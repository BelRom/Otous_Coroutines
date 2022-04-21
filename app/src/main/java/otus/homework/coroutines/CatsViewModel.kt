package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class CatsViewModel(
    private val catsService: CatsService,
    private val serviceImage: CatsImageService
) : ViewModel() {

    private val _resultData = MutableLiveData<Result>()

    val resultData: LiveData<Result>
        get() = _resultData

    private var presenterScope = PresenterScope()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        presenterScope.launch(exceptionHandler) {
            val fact = async { catsService.getCatFact() }
            val image = async { serviceImage.getImageCat() }
            _resultData.value = Success(FactAndImage(fact.await(), image.await()))
        }
    }

    override fun onCleared() {
        super.onCleared()
        presenterScope.coroutineContext.cancelChildren()
    }
}