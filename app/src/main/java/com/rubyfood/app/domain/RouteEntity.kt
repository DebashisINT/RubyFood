package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 08-11-2018.
 */
@Entity(tableName = AppConstant.ROUTE_TABLE)
class RouteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "route_id")
    var route_id: String? = null

    @ColumnInfo(name = "route_name")
    var route_name: String? = null

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false
}