package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: FactAndImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact.text
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(fact.imageUrlMeow.file).into(imageView)
    }

    override fun noErrorInternet() {
        val textError = context.getString(R.string.no_internet_error)
        Toast.makeText(context, textError, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: FactAndImage)

    fun noErrorInternet()
}