package gb.myhomework.convertfile.mvp.presenter

import gb.myhomework.convertfile.mvp.model.IConverter
import gb.myhomework.convertfile.mvp.model.Image
import gb.myhomework.convertfile.mvp.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter

class MainPresenter(private val converter: IConverter) :
    MvpPresenter<MainView>() {

    fun forConvertToClick() {
        viewState.pickImage()
    }

    private var conversionDisposable: Disposable? = null
    private var uiScheduler = AndroidSchedulers.mainThread()
    fun imageSelected(image: Image) {
        viewState.showConvertInProgress()
        conversionDisposable = converter.convert(image)
            .observeOn(uiScheduler)
            .subscribe(
                {
                    viewState.hideConverterInProgress()
                    viewState.showConverterSuccess()
                }, {
                    viewState.hideConverterInProgress()
                    viewState.showConvertError()
                }
            )
    }

    fun convertCancel() {
        conversionDisposable?.dispose()
        viewState.hideConverterInProgress()
        viewState.showConverterCancel()
    }
}