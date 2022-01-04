package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface EntityTypeDao {

    @Query("SELECT * FROM " + AppConstant.ENTITY_LIST_TABLE)
    fun getAll(): List<EntityTypeEntity>

    @Insert
    fun insert(vararg entityType: EntityTypeEntity)

    @Query("DELETE FROM " + AppConstant.ENTITY_LIST_TABLE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.ENTITY_LIST_TABLE + " where entity_id=:entity_id")
    fun getSingleItem(entity_id: String): EntityTypeEntity
}