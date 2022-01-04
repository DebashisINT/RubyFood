package com.rubyfood.features.dashboard.presentation.api.dashboardApi

import android.content.Context
import android.net.Uri
import com.rubyfood.app.FileUtils
import com.rubyfood.app.Pref
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.dashboard.presentation.DashboardActivity
import com.rubyfood.features.login.model.AlarmSelfieInput
import com.rubyfood.features.login.presentation.LoginActivity
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Saikat on 26-Jun-20.
 */
class DashboardRepo(val apiService: DashboardApi) {

    fun alarmWithSelfie(image: String, context: Context, reportId: String): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }

        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(AlarmSelfieInput(Pref.user_id!!, Pref.session_token!!, Pref.current_latitude,
                    Pref.current_longitude, AppUtils.getCurrentISODateTime(), reportId))
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.alarmSelfie(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun submitHomeLocReason(reason: String): Observable<BaseResponse> {
        return apiService.submitHomeLocReason(Pref.session_token!!, Pref.user_id!!, reason)
    }

    fun submiLogoutReason(reason: String): Observable<BaseResponse> {
        return apiService.submitLogoutReason(Pref.session_token!!, Pref.user_id!!, reason)
    }
}