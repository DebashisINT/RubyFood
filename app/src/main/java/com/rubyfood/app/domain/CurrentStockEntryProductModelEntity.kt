package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

@Entity(tableName = AppConstant.SHOP_CURRENT_STOCK_PRODUCTS_TABLE)
class CurrentStockEntryProductModelEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "user_id")
    var user_id: String? = null

    @ColumnInfo(name = "stock_id")
    var stock_id: String? = null

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "product_id")
    var product_id: String? = null

    @ColumnInfo(name = "product_stock_qty")
    var product_stock_qty: String? = null

    @ColumnInfo(name = "isUploaded")
    var isUploaded: Boolean = false

}