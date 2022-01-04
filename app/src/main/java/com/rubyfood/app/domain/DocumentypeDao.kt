package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface DocumentypeDao {

    @Query("SELECT * FROM " + AppConstant.DOCUMENT_TYPE_TABLE)
    fun getAll(): List<DocumentypeEntity>

    @Insert
    fun insert(vararg documentType: DocumentypeEntity)

    @Query("DELETE FROM " + AppConstant.DOCUMENT_TYPE_TABLE)
    fun delete()
}