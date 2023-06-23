package com.example.androidcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.androidcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.math.log

const val BASE_URL = "https://cat-fact.herokuapp.com"
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getCurrentData()

        binding.relativeLayout.setOnClickListener{
            getCurrentData()
        }

    }
    private fun getCurrentData(){

        binding.tvTextview.visibility = View.INVISIBLE
        binding.tvTime.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            try {
                val response = api.getCat().awaitResponse()
                if (response.isSuccessful){
                    val data = response.body()!!
                    Log.d(TAG, data.text)

                    withContext(Dispatchers.Main){
                        binding.tvTextview.visibility = View.VISIBLE
                        binding.tvTime.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        binding.tvTextview.text = data.text
                        binding.tvTime.text = data.createdAt
                    }
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext,"hata !!!",Toast.LENGTH_SHORT).show()
                }
            }

        }
     }

}