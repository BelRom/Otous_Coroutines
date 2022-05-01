package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*

class CatsViewModel(
    private val catsService: CatsService,
    private val serviceImage: CatsImageService
) : ViewModel() {

    private val _resultData = MutableLiveData<Result<FactAndImage>>()

    val resultData: LiveData<Result<FactAndImage>>
        get() = _resultData

    private var presenterScope = PresenterScope()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async { catsService.getCatFact() }
            val image = async { serviceImage.getImageCat() }
            _resultData.value = Result.Success(FactAndImage(fact.await(), image.await()))
        }
    }

    override fun onCleared() {
        super.onCleared()
        presenterScope.coroutineContext.cancelChildren()
    }
}

class CatsViewModelFactory(private val catsService: CatsService, private val serviceImage: CatsImageService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, serviceImage) as T
}