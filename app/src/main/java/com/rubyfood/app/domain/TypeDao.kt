package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 22-Jun-20.
 */
@Dao
interface TypeDao {

    @Insert
    fun insert(vararg type: TypeEntity)

    @Query("DELETE FROM " + AppConstant.TYPE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.TYPE)
    fun getAll(): List<TypeEntity>

    @Query("SELECT * FROM " + AppConstant.TYPE + " where type_id=:type_id")
    fun getSingleType(type_id: String): TypeEntity

    @Query("SELECT * FROM " + AppConstant.TYPE + " where activity_id=:activity_id")
    fun getTypeActivityWise(activity_id: String): List<TypeEntity>
}