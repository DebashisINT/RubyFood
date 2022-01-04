package com.rubyfood.features.nearbyuserlist.api

import com.rubyfood.app.Pref
import com.rubyfood.features.nearbyuserlist.model.NearbyUserResponseModel
import com.rubyfood.features.newcollection.model.NewCollectionListResponseModel
import com.rubyfood.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}