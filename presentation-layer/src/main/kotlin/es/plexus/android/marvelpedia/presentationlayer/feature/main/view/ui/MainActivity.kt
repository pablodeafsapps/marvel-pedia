package es.plexus.android.marvelpedia.presentationlayer.feature.main.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.base.BaseMvvmView
import es.plexus.android.marvelpedia.presentationlayer.base.ScreenState
import es.plexus.android.marvelpedia.presentationlayer.databinding.ActivityMainBinding
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.FailureVo
import es.plexus.android.marvelpedia.presentationlayer.feature.detail.view.ui.DetailActivity
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.adapter.CharacterListAdapter
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.state.MainState
import es.plexus.android.marvelpedia.presentationlayer.feature.main.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

const val INTENT_DATA_KEY = "characterItem"

/**
 * This [AppCompatActivity] represents the main feature of the application. It is here where the
 * data of interest is rendered right after the ViewModel has handed them over.
 *
 * The UI state is controlled thanks to the collection of a [viewModel] observable variable.
 */
@ExperimentalCoroutinesApi
class MainActivity :
    AppCompatActivity(),
    BaseMvvmView<MainViewModel, MainDomainLayerBridge<CharacterDataBoWrapper>, MainState> {

    override val viewModel: MainViewModel by viewModel()
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        initModel()
        initView()
        setContentView(viewBinding.root)
        viewModel.onViewCreated()
    }

    override fun processRenderState(renderState: MainState) {
        when (renderState) {
            is MainState.ShowCharacterList -> loadCharactersData(data = renderState.characterList)
            is MainState.ShowCharacterDetail -> navigateToDetailView(item = renderState.character)
            is MainState.ShowError -> showError(failure = renderState.failure)
        }
    }

    override fun initModel() {
        lifecycleScope.launch {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    is ScreenState.Idle -> hideLoading()
                    is ScreenState.Loading -> showLoading()
                    is ScreenState.Render<MainState> -> {
                        processRenderState(screenState.renderState)
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initView() {
        with(viewBinding.rvItems) {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = CharacterListAdapter(itemList = mutableListOf()) { character ->
                viewModel.onCharacterItemSelected(item = character)
            }
        }
    }

    private fun loadCharactersData(data: List<CharacterVo>) {
        with(viewBinding) {
            tvNoData.visibility = View.GONE
            (rvItems.adapter as? CharacterListAdapter)?.updateData(data)
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            pbLoading.visibility = View.VISIBLE
            rvItems.isEnabled = false
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            pbLoading.visibility = View.GONE
            rvItems.isEnabled = true
        }
    }

    private fun navigateToDetailView(item: CharacterVo) {
        val intent = Intent(this, DetailActivity::class.java)
            .putExtra(INTENT_DATA_KEY, item)
        startActivity(intent)
    }

    private fun showError(failure: FailureVo?) {
        if (failure?.getErrorMessage() != null) {
            viewBinding.tvNoData.visibility = View.VISIBLE
            Toast.makeText(this, failure.getErrorMessage(), Toast.LENGTH_SHORT).show()
        }
    }

}
