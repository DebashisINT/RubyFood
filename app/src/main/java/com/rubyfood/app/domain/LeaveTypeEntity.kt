package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 08-11-2018.
 */
@Entity(tableName = AppConstant.LEAVE_TYPE_TABLE)
class LeaveTypeEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "leave_type")
    var leave_type: String? = null
}