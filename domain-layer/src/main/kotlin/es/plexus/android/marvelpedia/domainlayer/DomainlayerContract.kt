package es.plexus.android.marvelpedia.domainlayer

import arrow.core.Either
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    }

    /**
     * Defines the requirements for an entity to become the 'data-layer' of the app
     */
    interface Datalayer {

        companion object {
            const val DATA_REPOSITORY_TAG = "dataRepository"
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
            suspend fun fetchCharacters(): Either<FailureBo, T>
        }

    }

}
