package com.rubyfood.features.averageshop.api

import com.rubyfood.app.NetworkConstant
import com.rubyfood.features.averageshop.model.ShopActivityRequest
import com.rubyfood.features.averageshop.model.ShopActivityResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Pratishruti on 07-12-2017.
 */
interface ShopActivityApi {
    @POST("Daywiseshop/Records")
    fun fetchShopActivity(@Body shopActivityRequest: ShopActivityRequest?): Observable<ShopActivityResponse>

    /**
     * Companion object to create the ShopActivityApi
     */
    companion object Factory {
        fun create(): ShopActivityApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(ShopActivityApi::class.java)
        }
    }
}