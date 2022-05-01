package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.service, diContainer.serviceImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
//        viewModel = ViewModelProvider(this).get(CatsViewModel::class.java)

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel?.onInitComplete()

        viewModel?.resultData?.observe(this) {
            when (it) {
                is Result.Success -> {
                    view.populate(it.result)
                }
                is Result.ErrorResult -> {
                    view.noErrorInternet()
                }
            }
        }
    }



    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}