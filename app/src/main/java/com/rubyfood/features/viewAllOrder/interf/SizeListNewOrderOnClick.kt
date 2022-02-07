package com.rubyfood.features.viewAllOrder.interf

import com.rubyfood.app.domain.NewOrderProductEntity
import com.rubyfood.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}