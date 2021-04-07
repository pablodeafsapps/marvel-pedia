package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmView
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.databinding.ActivityMainBinding
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo
import es.plexus.android.plexuschuck.presentationlayer.feature.detail.view.ui.DetailActivity
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeActionView
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeListAdapter
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.state.MainState
import es.plexus.android.plexuschuck.presentationlayer.feature.main.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

const val INTENT_DATA_KEY = "jokeItem"

/**
 * This [AppCompatActivity] represents the main feature of the application. It is here where the
 * data of interest is rendered right after the ViewModel has handed them over.
 *
 * The UI state is controlled thanks to the collection of a [viewModel] observable variable.
 */
@ExperimentalCoroutinesApi
class MainActivity :
    AppCompatActivity(),
    BaseMvvmView<MainViewModel, MainDomainLayerBridge<JokeBoWrapper>, MainState> {

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
            is MainState.ShowJokeList -> loadJokesData(data = renderState.jokeList)
            is MainState.ShowJokeDetail -> navigateToDetailActivity(item = renderState.joke)
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
            adapter = CnJokeListAdapter(itemList = mutableListOf()) { action ->
                when (action) {
                    is CnJokeActionView.JokeItemTapped -> viewModel.onJokeItemSelected(action.item)
                    CnJokeActionView.JokeItemLongSelected -> longToast("Item long-clicked")
                }
            }
        }
    }

    private fun loadJokesData(data: List<JokeVo>) {
        with(viewBinding) {
            tvNoData.visibility = View.GONE
            (rvItems.adapter as? CnJokeListAdapter)?.updateData(data)
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

    private fun navigateToDetailActivity(item: JokeVo) {
        startActivity<DetailActivity>(INTENT_DATA_KEY to item)
    }

    private fun showError(failure: FailureVo?) {
        if (failure?.getErrorMessage() != null) {
            viewBinding.tvNoData.visibility = View.VISIBLE
            toast(failure.getErrorMessage())
        }
    }

}
