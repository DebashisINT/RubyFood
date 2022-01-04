package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 18-09-2018.
 */
@Dao
interface AssignToPPDao {

    @Query("SELECT * FROM " + AppConstant.ASSIGNED_TO_PP_TABLE)
    fun getAll(): List<AssignToPPEntity>

    @Query("SELECT * FROM " + AppConstant.ASSIGNED_TO_PP_TABLE + " where pp_id=:pp_id")
    fun getSingleValue(pp_id: String): AssignToPPEntity

    /*@Query("delete " + AppConstant.ASSIGNED_TO_PP_TABLE + " where pp_id=:pp_id")
    fun deletePPId(pp_id: String)*/

    @Insert
    fun insert(vararg assignToPP: AssignToPPEntity)

    @Query("DELETE FROM " + AppConstant.ASSIGNED_TO_PP_TABLE)
    fun delete()

}