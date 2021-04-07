package es.plexus.android.plexuschuck.domainlayer.usecase

import arrow.core.Either
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper

const val FETCH_JOKES_UC_TAG = "fetchJokesUc"

/**
 * A use-case which allows to fetch a list of jokes from a repository
 *
 * @property [dataRepository] The repository responsible of bringing the required data
 */
class FetchJokesUc(private val dataRepository: DomainlayerContract.Datalayer.DataRepository<JokeBoWrapper>) :
    DomainlayerContract.Presentationlayer.UseCase<Any, JokeBoWrapper> {

    override suspend fun run(params: Any?): Either<FailureBo, JokeBoWrapper> = dataRepository.fetchJokes()

}
