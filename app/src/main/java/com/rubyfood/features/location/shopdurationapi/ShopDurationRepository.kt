package com.rubyfood.features.location.shopdurationapi

import com.rubyfood.app.Pref
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.location.model.MeetingDurationInputParams
import com.rubyfood.features.location.model.ShopDurationRequest
import com.rubyfood.features.location.model.VisitRemarksResponseModel
import io.reactivex.Observable

/**
 * Created by Pratishruti on 29-11-2017.
 */
class ShopDurationRepository(val apiService: ShopDurationApi) {
    fun shopDuration(shopDuration: ShopDurationRequest?): Observable<ShopDurationRequest> {
        return apiService.submitShopDuration(shopDuration)
    }

    fun meetingDuration(meetingDuration: MeetingDurationInputParams?): Observable<BaseResponse> {
        return apiService.submitMeetingDuration(meetingDuration)
    }

    fun getRemarksList(): Observable<VisitRemarksResponseModel> {
        return apiService.getRemarksList(Pref.session_token!!, Pref.user_id!!)
    }
}