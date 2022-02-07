package com.rubyfood.features.viewAllOrder.interf

import com.rubyfood.app.domain.NewOrderGenderEntity
import com.rubyfood.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}