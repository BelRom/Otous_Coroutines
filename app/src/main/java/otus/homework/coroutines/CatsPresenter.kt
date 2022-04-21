package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val serviceImage: CatsImageService
) {
    private var presenterScope = PresenterScope()

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async { catsService.getCatFact() }
                    val image = async { serviceImage.getImageCat() }
                    _catsView?.populate(FactAndImage(fact.await(), image.await()))
                }
            } catch (ex: Throwable) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.noErrorInternet()
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                    }
                }
            }

        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.coroutineContext.cancelChildren()
    }


}