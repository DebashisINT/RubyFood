package com.rubyfood.features.viewAllOrder.interf

import com.rubyfood.app.domain.NewOrderGenderEntity
import com.rubyfood.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}