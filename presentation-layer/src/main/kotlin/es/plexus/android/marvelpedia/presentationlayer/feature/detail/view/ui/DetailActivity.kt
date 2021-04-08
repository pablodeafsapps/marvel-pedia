package es.plexus.android.marvelpedia.presentationlayer.feature.detail.view.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.plexus.android.marvelpedia.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.base.BaseMvvmView
import es.plexus.android.marvelpedia.presentationlayer.base.ScreenState
import es.plexus.android.marvelpedia.presentationlayer.databinding.ActivityDetailBinding
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.FailureVo
import es.plexus.android.marvelpedia.presentationlayer.feature.detail.view.state.DetailState
import es.plexus.android.marvelpedia.presentationlayer.feature.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * This [AppCompatActivity] displays the details of the selected [CharacterVo]
 */
@ExperimentalCoroutinesApi
class DetailActivity :
    AppCompatActivity(),
    BaseMvvmView<DetailViewModel, BaseDomainLayerBridge.None, DetailState> {

    override val viewModel: DetailViewModel by viewModel()
    private lateinit var viewBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        initModel()
        setContentView(viewBinding.root)
    }

    override fun processRenderState(renderState: DetailState) {
        when (renderState) {
            is DetailState.ShowCharacterInfo -> loadCharacterItem(item = renderState.character)
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

    private fun loadCharacterItem(item: CharacterVo) {
//        with(viewBinding) {
//            tvId.text = getString(R.string.tv_detail_id, item.id?.toString() ?: "")
//            tvJoke.text = HtmlCompat.fromHtml(item.joke ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
//            tvCategories.text = item.categories.takeIf { it?.isNotEmpty() == true }?.toString()
//        }
    }

    private fun showLoading() {
        viewBinding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewBinding.pbLoading.visibility = View.GONE
    }

    private fun showError(failure: FailureVo?) {
        if (failure?.getErrorMessage() != null) {
            Toast.makeText(this, failure.getErrorMessage(), Toast.LENGTH_SHORT).show()
        }
    }

}
