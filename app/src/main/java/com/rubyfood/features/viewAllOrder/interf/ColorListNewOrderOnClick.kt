package com.rubyfood.features.viewAllOrder.interf

import com.rubyfood.app.domain.NewOrderColorEntity
import com.rubyfood.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}