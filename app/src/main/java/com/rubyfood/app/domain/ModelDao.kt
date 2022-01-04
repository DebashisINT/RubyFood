package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Dao
interface ModelDao {

    @Query("SELECT * FROM " + AppConstant.MODEL_TABLE)
    fun getAll(): List<ModelEntity>

    @Insert
    fun insertAll(vararg model: ModelEntity)

    @Query("DELETE FROM " + AppConstant.MODEL_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.MODEL_TABLE + " where model_id=:model_id")
    fun getSingleType(model_id: String): ModelEntity
}