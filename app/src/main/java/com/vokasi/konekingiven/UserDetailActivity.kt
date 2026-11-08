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
        val userName = intent.getStringExtra("USER_NAME")
        val userSalary = intent.getIntExtra("USER_SALARY", -1)
        val userAge = intent.getIntExtra("USER_AGE", -1)

        // Tampilkan data dari intent terlebih dahulu agar UI tidak kosong jika server kena rate limit 429
        if (!userName.isNullOrEmpty()) {
            binding.tvDetailId.text = "ID: $userId"
            binding.tvDetailName.text = userName
            binding.tvDetailSalary.text = "Gaji: $$userSalary"
            binding.tvDetailAge.text = "Umur: $userAge Tahun"
        }

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

                    binding.tvDetailId.text = "ID: ${employee.id}"
                    binding.tvDetailName.text = employee.employeeName
                    binding.tvDetailSalary.text = "Gaji: $${employee.employeeSalary}"
                    binding.tvDetailAge.text = "Umur: ${employee.employeeAge} Tahun"
                } else {
                    // Menampilkan pesan kode error server (misal 429 Rate Limit) tanpa mengosongkan UI
                    Toast.makeText(this@UserDetailActivity,
                        "Server Respon: ${response.code()} (Rate Limit). Data dimuat dari list.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity,
                    "Koneksi Error: ${t.localizedMessage}. Data dimuat dari list.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}
