package ru.drrey.babyname.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Rest client
 */
class RestClient {
    val nameService: NameApi

    //client
    init {
        val gson = GsonBuilder()
            // .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            // .setLenient()
            .create()

        val client = OkHttpClient()
        client.followRedirects()

        val retrofit = Retrofit.Builder()
            .client(client)
            // .baseUrl(GoodGameApp.getContext().getString(R.string.gg_api_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        nameService = retrofit.create(NameApi::class.java)
    }
}
