package com.rubyfood.features.orderList.model

import com.rubyfood.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}