package com.rubyfood.features.averageshop.api

import com.rubyfood.features.averageshop.model.ShopActivityRequest
import com.rubyfood.features.averageshop.model.ShopActivityResponse
import io.reactivex.Observable

/**
 * Created by Pratishruti on 07-12-2017.
 */
class ShopActivityRepository (val apiService: ShopActivityApi) {
    fun fetchShopActivity(shopActivityReq: ShopActivityRequest?): Observable<ShopActivityResponse> {
        return apiService.fetchShopActivity(shopActivityReq)
    }
}