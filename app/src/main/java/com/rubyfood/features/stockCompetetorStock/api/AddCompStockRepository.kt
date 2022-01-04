package com.rubyfood.features.stockCompetetorStock.api

import com.rubyfood.base.BaseResponse
import com.rubyfood.features.orderList.model.NewOrderListResponseModel
import com.rubyfood.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.rubyfood.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class AddCompStockRepository(val apiService:AddCompStockApi){

    fun addCompStock(shopAddCompetetorStockRequest: ShopAddCompetetorStockRequest): Observable<BaseResponse> {
        return apiService.submShopCompStock(shopAddCompetetorStockRequest)
    }

    fun getCompStockList(sessiontoken: String, user_id: String, date: String): Observable<CompetetorStockGetData> {
        return apiService.getCompStockList(sessiontoken, user_id, date)
    }
}