package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 31-08-2018.
 */
@Entity(tableName = AppConstant.WORK_TYPE_TABLE)
class WorkTypeEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    var ID: Int = 0

    @ColumnInfo(name = "Descrpton")
    var Descrpton: String? = null

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false
}