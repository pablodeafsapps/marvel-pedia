package es.plexus.android.plexuschuck.presentationlayer.feature.detail.view.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.R
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmView
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.databinding.ActivityDetailBinding
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo
import es.plexus.android.plexuschuck.presentationlayer.feature.detail.view.state.DetailState
import es.plexus.android.plexuschuck.presentationlayer.feature.detail.viewmodel.DetailViewModel
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.ui.INTENT_DATA_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * This [AppCompatActivity] displays the details of the selected [JokeVo]
 */
@ExperimentalCoroutinesApi
class DetailActivity :
    AppCompatActivity(),
    BaseMvvmView<DetailViewModel, BaseDomainLayerBridge.None, DetailState> {

    override val viewModel: DetailViewModel by viewModel()
    private lateinit var viewBinding: ActivityDetailBinding
    private var jokeItem: JokeVo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        initModel()
        setContentView(viewBinding.root)
        jokeItem = intent.getParcelableExtra(INTENT_DATA_KEY) as? JokeVo
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewCreated(jokeItem)
    }

    override fun processRenderState(renderState: DetailState) {
        when (renderState) {
            is DetailState.ShowJokeInfo -> loadJokeItem(item = renderState.joke)
            is DetailState.ShowError -> showError(failure = renderState.failure)
        }
    }

    override fun initModel() {
        lifecycleScope.launch {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    is ScreenState.Idle -> hideLoading()
                    is ScreenState.Loading -> showLoading()
                    is ScreenState.Render<DetailState> -> processRenderState(screenState.renderState)
                }
            }
        }
    }

    private fun loadJokeItem(item: JokeVo) {
        with(viewBinding) {
            tvId.text = getString(R.string.tv_detail_id, item.id?.toString() ?: "")
            tvJoke.text = HtmlCompat.fromHtml(item.joke ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
            tvCategories.text = item.categories.takeIf { it?.isNotEmpty() == true }?.toString()
        }
    }

    private fun showLoading() {
        viewBinding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewBinding.pbLoading.visibility = View.GONE
    }

    private fun showError(failure: FailureVo?) {
        if (failure?.getErrorMessage() != null) {
            toast(failure.getErrorMessage())
        }
    }

}
