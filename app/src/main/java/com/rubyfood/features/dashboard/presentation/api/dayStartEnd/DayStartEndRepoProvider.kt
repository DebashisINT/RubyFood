package com.rubyfood.features.dashboard.presentation.api.dayStartEnd

import com.rubyfood.features.stockCompetetorStock.api.AddCompStockApi
import com.rubyfood.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}