package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Entity(tableName = AppConstant.PRIMARY_APPLICATION_TABLE)
class PrimaryAppEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "primary_app_id")
    var primary_app_id: String? = null

    @ColumnInfo(name = "primary_app_name")
    var primary_app_name: String? = null
}