package com.rubyfood.features.viewAllOrder.orderOptimized

import com.rubyfood.app.domain.ProductOnlineRateTempEntity
import com.rubyfood.base.BaseResponse
import com.rubyfood.features.login.model.productlistmodel.ProductRateDataModel
import java.io.Serializable

class ProductRateOnlineListResponseModel: BaseResponse(), Serializable {
    var product_rate_list: ArrayList<ProductOnlineRateTempEntity>? = null
}