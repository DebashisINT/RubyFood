package com.rubyfood.app.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rubyfood.app.AppConstant

@Entity(tableName = AppConstant.ENTITY_LIST_TABLE)
class EntityTypeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "entity_id")
    var entity_id: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null
}