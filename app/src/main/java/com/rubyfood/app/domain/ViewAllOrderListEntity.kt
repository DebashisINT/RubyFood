package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant.SHOP_ACTIVITY

/**
 * Created by Pratishruti on 07-12-2017.
 */
@Entity(tableName = SHOP_ACTIVITY)
class ViewAllOrderListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    var itemId: Int = 0

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "amount")
    var amount: String? = null

}