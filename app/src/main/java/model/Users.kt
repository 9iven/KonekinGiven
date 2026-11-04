package model // 👈 Make sure this package declaration matches your folder name

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Data>, // Must match your list item model
    @SerializedName("message") val message: String
)

