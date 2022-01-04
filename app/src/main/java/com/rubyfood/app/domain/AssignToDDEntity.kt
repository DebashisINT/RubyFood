package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 18-09-2018.
 */
@Entity(tableName = AppConstant.ASSIGNED_TO_DD_TABLE)
class AssignToDDEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int? = null

    @ColumnInfo(name = "dd_id")
    var dd_id: String? = null

    @ColumnInfo(name = "dd_name")
    var dd_name: String? = null

    @ColumnInfo(name = "dd_phn_no")
    var dd_phn_no: String? = null

    @ColumnInfo(name = "pp_id")
    var pp_id: String? = null

    @ColumnInfo(name = "type_id")
    var type_id: String? = null
}