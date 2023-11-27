package com.rubyfood.features.newcollectionreport

import com.rubyfood.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}