package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant.CITY_TABLE


/**
 * Created by Pratishruti on 07-12-2017.
// */
@Entity(tableName = CITY_TABLE)
class CityListEntity {

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "Id")
//    var id: Int? = 0


    @ColumnInfo(name = "state_id")
    var state_id: String? = null

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "city_id")
    var city_id: Int = 0

    @ColumnInfo(name = "city_name")
    var city_name: String? = null

}