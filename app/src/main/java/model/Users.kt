package model

data class Users(
    @SerializedName("data")

    val data: List<Data>
)
