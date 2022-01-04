package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

@Dao
interface DealerDao {
    @Query("SELECT * FROM " + AppConstant.DEALER_TABLE)
    fun getAll(): List<DealerEntity>

    @Insert
    fun insert(vararg dealer: DealerEntity)

    @Query("DELETE FROM " + AppConstant.DEALER_TABLE)
    fun delete()

    @Query("SELECT * FROM " + AppConstant.DEALER_TABLE + " where dealer_id=:dealer_id")
    fun getSingleItem(dealer_id: String): DealerEntity
}