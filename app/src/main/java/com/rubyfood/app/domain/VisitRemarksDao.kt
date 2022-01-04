package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface VisitRemarksDao {

    @Query("SELECT * FROM " + AppConstant.VISIT_REMARKS_TABLE)
    fun getAll(): List<VisitRemarksEntity>

    @Insert
    fun insertAll(vararg visitRemarks: VisitRemarksEntity)

    @Query("DELETE FROM " + AppConstant.VISIT_REMARKS_TABLE)
    fun delete()
}