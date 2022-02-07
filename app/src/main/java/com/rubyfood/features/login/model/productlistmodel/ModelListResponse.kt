package com.rubyfood.features.login.model.productlistmodel

import com.rubyfood.app.domain.ModelEntity
import com.rubyfood.app.domain.ProductListEntity
import com.rubyfood.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}