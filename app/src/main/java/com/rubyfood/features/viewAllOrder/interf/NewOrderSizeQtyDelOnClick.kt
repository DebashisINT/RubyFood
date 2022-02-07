package com.rubyfood.features.viewAllOrder.interf

import com.rubyfood.app.domain.NewOrderGenderEntity
import com.rubyfood.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}