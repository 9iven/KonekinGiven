package com.vokasi.konekingiven

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vokasi.konekingiven.databinding.ActivityMainBinding
import model.Users
import network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()
        val response = client.getAllUsers()
        val userList = ArrayList<String>()

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                val responseBody = response.body()
                if (responseBody != null && response.isSuccessful) {

                    // Map names to the ArrayList for display
                    for (i in responseBody.data) {
                        userList.add(i.employeeName)
                    }

                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        userList
                    )
                    binding.lvNama.adapter = listAdapter

                    // Setup the Click Listener on the ListView
                    binding.lvNama.setOnItemClickListener { _, _, position, _ ->
                        val selectedUser = responseBody.data[position]

                        // Kirim data lengkap ke UserDetailActivity agar data tetap tampil saat server 429
                        val intent = Intent(this@MainActivity, UserDetailActivity::class.java).apply {
                            putExtra("USER_ID", selectedUser.id)
                            putExtra("USER_NAME", selectedUser.employeeName)
                            putExtra("USER_SALARY", selectedUser.employeeSalary)
                            putExtra("USER_AGE", selectedUser.employeeAge)
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Server Respon: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi error: ${t.localizedMessage}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}
