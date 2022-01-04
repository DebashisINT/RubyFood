package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 14-May-20.
 */
@Entity(tableName = AppConstant.AREA_LIST_TABLE)
class AreaListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "area_id")
    var area_id: String? = null

    @ColumnInfo(name = "area_name")
    var area_name: String? = null
}