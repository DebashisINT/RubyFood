package com.rubyfood.features.stockAddCurrentStock.api

import com.rubyfood.base.BaseResponse
import com.rubyfood.features.location.model.ShopRevisitStatusRequest
import com.rubyfood.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.rubyfood.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.rubyfood.features.stockAddCurrentStock.model.CurrentStockGetData
import com.rubyfood.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}