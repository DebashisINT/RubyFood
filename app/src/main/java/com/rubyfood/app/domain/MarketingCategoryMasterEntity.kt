package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant.*


/**
 * Created by Pratishruti on 07-12-2017.
// */
@Entity(tableName = MARKETING_CATEGORY_MASTER_TABLE)
class MarketingCategoryMasterEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int? = null

    @ColumnInfo(name = "material_id")
    var material_id:String? = null

    @ColumnInfo(name = "material_name")
    var material_name: String? = null

    @ColumnInfo(name = "type_id")
    var type_id: String? = null

}