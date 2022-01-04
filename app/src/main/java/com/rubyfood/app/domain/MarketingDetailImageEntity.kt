package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant.*


/**
 * Created by Pratishruti on 07-12-2017.
// */
@Entity(tableName = MARKETING_IMAGE)
class MarketingDetailImageEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int? = null

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "marketing_img")
    var marketing_img: String? = null

    @ColumnInfo(name = "image_id")
    var image_id: String? = null
}