package com.rubyfood.features.viewAllOrder.model

import com.rubyfood.features.stockCompetetorStock.ShopAddCompetetorStockProductList
import com.rubyfood.features.viewAllOrder.orderNew.NeworderScrCartFragment

class NewOrderSaveApiModel {
    var user_id: String? = null
    var session_token: String? = null
    var order_id: String? = null
    var shop_id: String? = null
    var order_date: String? = null
    var product_list: List<NeworderScrCartFragment.NewOrderRoomData>? = null
}