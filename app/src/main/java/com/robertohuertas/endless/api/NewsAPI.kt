package com.robertohuertas.endless.api

import com.robertohuertas.endless.EndlessService
import com.robertohuertas.endless.models.Covid
import com.robertohuertas.endless.models.DistrictResponse
import com.robertohuertas.endless.models.State
import com.robertohuertas.endless.models.StateResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/appointment/sessions/public/findByDistrict")
   suspend fun getcov(
        @Query("district_id")
        distid: String = "512",
        @Query("date")
        dateid: String = "31-03-2021"

    ): Response<Covid>

   @Headers(
       "accept: application/json",
       "Accept-Language:hi_IN",
       "X-Requested-With: XMLHttpRequest",
       "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
    @GET("v2/admin/location/states")
    suspend fun getStates(): Response<StateResponse>


    @Headers(
        "accept: application/json",
        "Accept-Language:hi_IN",
        "X-Requested-With: XMLHttpRequest",
        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
    @GET("v2/admin/location/districts/{state_id}")
    suspend fun getDistricts(
        @Path("state_id")
        distid: String = "16"
    ): Response<DistrictResponse>
}
class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("https://cdn-api.co-vin.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}