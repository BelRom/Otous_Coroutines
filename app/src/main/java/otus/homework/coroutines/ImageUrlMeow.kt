package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImageUrlMeow(
	@field:SerializedName("file")
	val file: String,
)