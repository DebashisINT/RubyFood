package com.rubyfood.features.viewAllOrder.model

import com.rubyfood.app.domain.NewOrderColorEntity
import com.rubyfood.app.domain.NewOrderGenderEntity
import com.rubyfood.app.domain.NewOrderProductEntity
import com.rubyfood.app.domain.NewOrderSizeEntity
import com.rubyfood.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

