package com.rubyfood.app.domain

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Dao
interface FunnelStageDao {

    @Query("SELECT * FROM " + AppConstant.FUNNEL_STAGE_TABLE)
    fun getAll(): List<FunnelStageEntity>

    @Insert
    fun insertAll(vararg funnel: FunnelStageEntity)

    @Query("DELETE FROM " + AppConstant.FUNNEL_STAGE_TABLE)
    fun deleteAll()

    @Query("SELECT * FROM " + AppConstant.FUNNEL_STAGE_TABLE + " where funnel_stage_id=:funnel_stage_id")
    fun getSingleType(funnel_stage_id: String): FunnelStageEntity
}