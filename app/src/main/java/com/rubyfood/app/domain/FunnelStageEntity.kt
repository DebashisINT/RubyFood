package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

/**
 * Created by Saikat on 05-Jun-20.
 */
@Entity(tableName = AppConstant.FUNNEL_STAGE_TABLE)
class FunnelStageEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "funnel_stage_id")
    var funnel_stage_id: String? = null

    @ColumnInfo(name = "funnel_stage_name")
    var funnel_stage_name: String? = null
}