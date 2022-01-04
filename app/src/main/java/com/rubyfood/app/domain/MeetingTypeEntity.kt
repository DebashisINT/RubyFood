package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 17-01-2020.
 */
@Entity(tableName = AppConstant.MEETING_TYPE)
class MeetingTypeEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "typeId")
    var typeId: Int = 0

    @ColumnInfo(name = "typeText")
    var typeText: String? = null
}