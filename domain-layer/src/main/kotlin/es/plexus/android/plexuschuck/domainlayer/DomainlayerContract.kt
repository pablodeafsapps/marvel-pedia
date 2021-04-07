package es.plexus.android.plexuschuck.domainlayer

import arrow.core.Either
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Gathers all protocols to interact with the 'domain-layer'.
 *
 * Includes several interfaces which define the contracts to comply, both on the 'presentation' and
 * the 'data' side.
 */
interface DomainlayerContract {

    /**
     * Defines the requirements for an entity to become the 'presentation-layer' of the app
     */
    interface Presentationlayer {

        /**
         * Defines baseline use-case using Kotlin Coroutines
         */
        interface UseCase<in T, out S> {
            /**
             * Initiates the use-case using certain arguments related to Kotlin Coroutines
             *
             * @param [scope] the [CoroutineScope] used, which will cancel and terminate this entity when required
             * @param [params] arguments used in the query. It is null by default.
             * @param [onResult] a callback to return a value at some point
             * @param [dispatcherWorker] the dispatcher used to perform the use-case (Dispatchers.IO by default)
             */
            fun invoke(
                scope: CoroutineScope,
                params: T? = null,
                onResult: (Either<FailureBo, S>) -> Unit,
                dispatcherWorker: CoroutineDispatcher = Dispatchers.IO
            ) {
                // Task undertaken in a dispatcher worker and once completed, result sent in the scope thread
                scope.launch { withContext(dispatcherWorker) { onResult(run(params)) } }
            }

            /**
             * Executes the previously defined use-cause
             *
             * @param [param] arguments used in the query
             * @return An [S] value if successful or a [FailureBo] otherwise
             */
            suspend fun run(params: T? = null): Either<FailureBo, S>
        }

        /**
         * Defines baseline use-case using Kotlin flow
         */
        @ExperimentalCoroutinesApi
        interface FlowUseCase<in T, out S> {
            /**
             * Initiates the use-case using certain arguments related to Kotlin Coroutines
             *
             * @param [scope] the [CoroutineScope] used, which will cancel and terminate this entity when required
             * @param [params] arguments used in the query. It is null by default.
             * @param [onResult] a callback to return a value at some point
             * @param [dispatcherWorker] the dispatcher used to perform the use-case (Dispatchers.IO by default)
             */
            fun invoke(
                scope: CoroutineScope,
                params: List<T>,
                onResult: (Either<FailureBo, S>) -> Unit,
                dispatcherWorker: CoroutineDispatcher = Dispatchers.IO
            ) {
                // Task undertaken in a dispatcher worker thread
                val jobFlow =
                    flow {
                        params.forEach { param -> emit(run(param)) }
                    }.flowOn(Dispatchers.Default)
                // once completed, result sent in the scope thread
                scope.launch { withContext(dispatcherWorker) { jobFlow.collect { onResult(it) } } }
            }

            /**
             * Executes the previously defined use-cause
             *
             * @param [param] arguments used in the query
             * @return An [S] value if successful or a [FailureBo] otherwise
             */
            suspend fun run(param: T): Either<FailureBo, S>
        }

        /**
         * Defines baseline use-case using Kotlin channelFlow
         */
        @ExperimentalCoroutinesApi
        interface ChannelFlowUseCase<in T, out S> {
            /**
             * Initiates the use-case using certain arguments related to Kotlin Coroutines
             *
             * @param [scope] the [CoroutineScope] used, which will cancel and terminate this entity when required
             * @param [params] arguments used in the query. It is null by default.
             * @param [onResult] a callback to return a value at some point
             * @param [dispatcherWorker] the dispatcher used to perform the use-case (Dispatchers.IO by default)
             */
            fun invoke(
                scope: CoroutineScope,
                params: List<T>,
                onResult: (Either<FailureBo, S>) -> Unit,
                dispatcherWorker: CoroutineDispatcher = Dispatchers.IO
            ) {
                // task undertaken in a worker thread
                val jobChannelFlow =
                    channelFlow {
                        params.forEach { param -> send(run(param)) }
                        awaitClose { println("ChannelFlowUseCase, All done!") }
                    }.flowOn(Dispatchers.Default)
                // once completed, result sent in the scope thread
                scope.launch { withContext(dispatcherWorker) { jobChannelFlow.collect { onResult(it) } } }
            }

            /**
             * Executes the previously defined use-cause
             *
             * @param [param] arguments used in the query
             * @return An [S] value if successful or a [FailureBo] otherwise
             */
            suspend fun run(param: T): Either<FailureBo, S>

        }

        /**
         * Defines baseline use-case using Kotlin callbackFlow
         */
        @ExperimentalCoroutinesApi
        interface CallbackFlowUseCase<in T, out S> {
            /**
             * Initiates the use-case using certain arguments related to Kotlin Coroutines
             *
             * @param [scope] the [CoroutineScope] used, which will cancel and terminate this entity when required
             * @param [params] arguments used in the query. It is null by default.
             * @param [onResult] a callback to return a value at some point
             * @param [dispatcherWorker] the dispatcher used to perform the use-case (Dispatchers.IO by default)
             */
            fun invoke(
                scope: CoroutineScope,
                params: List<T>,
                onResult: (Either<FailureBo, S>) -> Unit,
                dispatcherWorker: CoroutineDispatcher = Dispatchers.IO
            ) {
                // task undertaken in a worker thread
                val jobCallbackFlow =
                    callbackFlow {
                        params.forEach { param -> run(param) { offer(it) } }
                        awaitClose { println("CallbackFlowUseCase, All done!") }
                    }.flowOn(Dispatchers.Default)
                // once completed, result sent in the scope thread
                scope.launch { withContext(dispatcherWorker) { jobCallbackFlow.collect { onResult(it) } } }
            }

            /**
             * Executes the previously defined use-cause
             *
             * @param [param] arguments used in the query
             * @param [callback] which allows to return an [S] value at some point
             */
            suspend fun run(param: T, callback: (Either<FailureBo, S>) -> Unit): Unit
        }

    }

    /**
     * Defines the requirements for an entity to become the 'data-layer' of the app
     */
    interface Datalayer {

        companion object {
            const val AUTHENTICATION_REPOSITORY_TAG = "authenticationRepository"
            const val DATA_REPOSITORY_TAG = "dataRepository"
        }

        /**
         * Represents the requirements to comply for an authentication repository
         */
        interface AuthenticationRepository<in T, out S> {
            /**
             * Logs-in a user according to certain info parameters
             *
             * @param [params] user info for login
             * @return A [S] data if it is successful or a [FailureBo] otherwise
             */
            suspend fun loginUser(params: T): Either<FailureBo, S>

            /**
             * Registers a user according to certain info parameters
             *
             * @param [params] user info for registration
             * @return A [S] data if it is successful or a [FailureBo] otherwise
             */
            suspend fun registerUser(params: T): Either<FailureBo, S>
        }

        /**
         * Represents the requirements to comply for a data repository
         */
        interface DataRepository<out T> {
            /**
             * Fetches joke-related data according to [T] data type
             *
             * @return A [T] data if it is successful or a [FailureBo] otherwise
             */
            suspend fun fetchJokes(): Either<FailureBo, T>
        }

    }

}
