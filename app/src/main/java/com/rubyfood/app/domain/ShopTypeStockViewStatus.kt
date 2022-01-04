package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

@Entity(tableName = AppConstant.SHOP_TYPE_STOCK_VIEW_STATUS)
class ShopTypeStockViewStatus {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "shoptype_id")
    var shoptype_id: String? = null

    @ColumnInfo(name = "shoptype_name")
    var shoptype_name: String? = null

    @ColumnInfo(name = "CurrentStockEnable")
    var CurrentStockEnable: Int? = 0

    @ColumnInfo(name = "CompetitorStockEnable")
    var CompetitorStockEnable: Int? = 0

}