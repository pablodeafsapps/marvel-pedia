package es.plexus.android.marvelpedia.presentationlayer.base

/**
 * This sealed class is the baseline upon which any screen state is constructed
 *
 * @since 1.0
 */
sealed class ScreenState<out T : BaseState> {
    object Idle : ScreenState<Nothing>()
    object Loading : ScreenState<Nothing>()
    class Render<out T : BaseState>(val renderState: T) : ScreenState<T>()
}
