package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 03-Jul-20.
 */
@Dao
interface TeamAreaDao {

    @Query("SELECT * FROM " + AppConstant.MEMBER_AREA_TABLE)
    fun getAll(): List<TeamAreaEntity>

    @Insert
    fun insertAll(vararg area: TeamAreaEntity)

    @Query("DELETE FROM " + AppConstant.MEMBER_AREA_TABLE)
    fun deleteAll()
}