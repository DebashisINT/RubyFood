package com.rubyfood.features.location.shopRevisitStatus

import com.rubyfood.base.BaseResponse
import com.rubyfood.features.location.model.ShopDurationRequest
import com.rubyfood.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}