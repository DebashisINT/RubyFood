package com.rubyfood.features.login

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant


/**
 * Created by Kinsuk on 16-11-2017.
 */
@Entity(tableName = AppConstant.ATTENDANCE_TABLE)
class UserLoginDataEntity {

    @PrimaryKey(autoGenerate = true)
    var entryId: Int = 0

    @ColumnInfo(name = "logindate")
    var logindate: String = ""

    @ColumnInfo(name = "logintime")
    var logintime: String = ""

    @ColumnInfo(name = "logouttime")
    var logouttime: String = ""

    @ColumnInfo(name = "duration")
    var duration: String = ""

    @ColumnInfo(name = "userId")
    var userId: String = ""

    @ColumnInfo(name = "Isonleave")
    var Isonleave: String? = null

    @ColumnInfo(name = "logindate_number")
    var logindate_number: Long? = null

//    @ColumnInfo(name = "login_date")
//    var login_date: Date? = null


}