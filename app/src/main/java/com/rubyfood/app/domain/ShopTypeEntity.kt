package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 01-Jun-20.
 */
@Entity(tableName = AppConstant.SHOP_TYPE)
class ShopTypeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "shoptype_id")
    var shoptype_id: String? = null

    @ColumnInfo(name = "shoptype_name")
    var shoptype_name: String? = null

}