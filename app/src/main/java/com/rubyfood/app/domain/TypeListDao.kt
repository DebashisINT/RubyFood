package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 22-Jun-20.
 */
@Dao
interface TypeListDao {

    @Insert
    fun insert(vararg type: TypeListEntity)

    @Query("DELETE FROM " + AppConstant.TYPE_TABLE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.TYPE_TABLE)
    fun getAll(): List<TypeListEntity>

    @Query("SELECT * FROM " + AppConstant.TYPE_TABLE + " where type_id=:type_id")
    fun getSingleType(type_id: String): TypeListEntity
}