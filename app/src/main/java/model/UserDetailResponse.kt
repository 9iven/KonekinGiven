package model

import com.google.gson.annotations.SerializedName

// ni untuk menampung Satu Karyawan Saja (Dipakai di UserDetailActivity)
data class UserDetailResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: Data,
    @SerializedName("message") val message: String
)