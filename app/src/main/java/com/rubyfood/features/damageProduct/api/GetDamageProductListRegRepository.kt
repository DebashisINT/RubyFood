package com.rubyfood.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.rubyfood.app.FileUtils
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.NewQuotation.model.*
import com.rubyfood.features.addshop.model.AddShopRequestData
import com.rubyfood.features.addshop.model.AddShopResponse
import com.rubyfood.features.damageProduct.model.DamageProductResponseModel
import com.rubyfood.features.damageProduct.model.delBreakageReq
import com.rubyfood.features.damageProduct.model.viewAllBreakageReq
import com.rubyfood.features.login.model.userconfig.UserConfigResponseModel
import com.rubyfood.features.myjobs.model.WIPImageSubmit
import com.rubyfood.features.photoReg.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GetDamageProductListRegRepository(val apiService : GetDamageProductListApi) {

    fun viewBreakage(req: viewAllBreakageReq): Observable<DamageProductResponseModel> {
        return apiService.viewBreakage(req)
    }

    fun delBreakage(req: delBreakageReq): Observable<BaseResponse>{
        return apiService.BreakageDel(req.user_id!!,req.breakage_number!!,req.session_token!!)
    }

}