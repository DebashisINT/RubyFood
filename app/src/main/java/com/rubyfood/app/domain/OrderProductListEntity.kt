package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 12-11-2018.
 */
@Entity(tableName = AppConstant.ORDER_PRODUCT_LIST_TABLE)
class OrderProductListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "product_id")
    var product_id: String? = null

    @ColumnInfo(name = "product_name")
    var product_name: String? = null

    @ColumnInfo(name = "brand_id")
    var brand_id: String? = null

    @ColumnInfo(name = "brand")
    var brand: String? = null

    @ColumnInfo(name = "category_id")
    var category_id: String? = null

    @ColumnInfo(name = "category")
    var category: String? = null

    @ColumnInfo(name = "watt_id")
    var watt_id: String? = null

    @ColumnInfo(name = "watt")
    var watt: String? = null

    @ColumnInfo(name = "qty")
    var qty: String? = null

    @ColumnInfo(name = "rate")
    var rate: String? = null

    @ColumnInfo(name = "total_price")
    var total_price: String? = null

    @ColumnInfo(name = "order_id")
    var order_id: String? = null

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null
}