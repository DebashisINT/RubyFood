package com.rubyfood.features.location.api

import com.rubyfood.app.Pref
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.location.model.AppInfoInputModel
import com.rubyfood.features.location.model.AppInfoResponseModel
import com.rubyfood.features.location.model.ShopDurationRequest
import com.rubyfood.features.location.shopdurationapi.ShopDurationApi
import io.reactivex.Observable

/**
 * Created by Saikat on 17-Aug-20.
 */
class LocationRepo(val apiService: LocationApi) {
    fun appInfo(appInfo: AppInfoInputModel?): Observable<BaseResponse> {
        return apiService.submitAppInfo(appInfo)
    }

    fun getAppInfo(): Observable<AppInfoResponseModel> {
        return apiService.getAppInfo(Pref.session_token!!, Pref.user_id!!)
    }
}