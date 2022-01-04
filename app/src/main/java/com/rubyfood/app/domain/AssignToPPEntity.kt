package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 18-09-2018.
 */
@Entity(tableName = AppConstant.ASSIGNED_TO_PP_TABLE)
class AssignToPPEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int? = null

    @ColumnInfo(name = "pp_id")
    var pp_id: String? = null

    @ColumnInfo(name = "pp_name")
    var pp_name: String? = null

    @ColumnInfo(name = "pp_phn_no")
    var pp_phn_no: String? = null
}