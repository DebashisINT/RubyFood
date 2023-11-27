package com.rubyfood.features.photoReg.present

import com.rubyfood.app.domain.ProspectEntity
import com.rubyfood.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}