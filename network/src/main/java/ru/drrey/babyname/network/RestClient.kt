package ru.drrey.babyname.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Rest client
 */
class RestClient {
    private val nameService: NameApi

    //client
    init {
        val gson = GsonBuilder().create()
        val client = OkHttpClient.Builder().followRedirects(true).build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        nameService = retrofit.create(NameApi::class.java)
    }
}
