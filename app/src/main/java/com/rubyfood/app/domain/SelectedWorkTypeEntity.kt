package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 30-11-2018.
 */
@Entity(tableName = AppConstant.SELECTED_WORK_TYPE_TABLE)
class SelectedWorkTypeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "work_type_id")
    var work_type_id: Int = 0

    @ColumnInfo(name = "ID")
    var ID: Int = 0

    @ColumnInfo(name = "Descrpton")
    var Descrpton: String? = null

    @ColumnInfo(name = "date")
    var date: String? = null
}