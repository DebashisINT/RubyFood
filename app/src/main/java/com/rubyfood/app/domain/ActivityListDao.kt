package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 10-Jul-20.
 */
@Dao
interface ActivityListDao {

    @Query("SELECT * FROM " + AppConstant.ACTIVITY_LIST)
    fun getAll(): List<ActivityListEntity>

    @Insert
    fun insertAll(vararg activity: ActivityListEntity)

    @Query("DELETE FROM " + AppConstant.ACTIVITY_LIST)
    fun deleteAll()

}