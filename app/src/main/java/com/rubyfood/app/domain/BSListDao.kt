package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 12-Jun-20.
 */
@Dao
interface BSListDao {

    @Query("SELECT * FROM " + AppConstant.BSLIST_TABLE)
    fun getAll(): List<BSListEntity>

    @Insert
    fun insertAll(vararg bsList: BSListEntity)

    @Query("DELETE FROM " + AppConstant.BSLIST_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.BSLIST_TABLE + " where bs_id=:bs_id")
    fun getSingleType(bs_id: String): BSListEntity

}