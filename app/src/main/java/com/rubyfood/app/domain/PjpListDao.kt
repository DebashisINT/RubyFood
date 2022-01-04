package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 02-Jun-20.
 */
@Dao
interface PjpListDao {

    @Insert
    fun insert(vararg pjpList: PjpListEntity)

    @Query("DELETE FROM " + AppConstant.PJP_LIST_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.PJP_LIST_TABLE)
    fun getAll(): List<PjpListEntity>
}