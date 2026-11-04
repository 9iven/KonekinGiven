package com.vokasi.konekingiven

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vokasi.konekingiven.databinding.ActivityUserDetailBinding
import model.UserDetailResponse
import network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            fetchUserDetail(userId)
        } else {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDetail(id: Int) {
        val client = ApiClient.getInstance()

        client.getUserById(id).enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val employee = responseBody.data

                    binding.tvDetailId.text = "ID Karyawan: ${employee.id}"
                    binding.tvDetailName.text = employee.employeeName
                    binding.tvDetailSalary.text = "Gaji: $${employee.employeeSalary}"
                    binding.tvDetailAge.text = "Umur: ${employee.employeeAge} Tahun"
                } else {
                    // Menampilkan kode error asli dari server (contoh: 429 atau 404)
                    Toast.makeText(this@UserDetailActivity,
                        "Gagal memuat data. Server Respon: ${response.code()}",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                // Menampilkan detail error koneksi
                Toast.makeText(this@UserDetailActivity,
                    "Koneksi Error: ${t.localizedMessage}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

}