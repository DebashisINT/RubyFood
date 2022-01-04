package com.rubyfood.features.stockAddCurrentStock.api

import com.rubyfood.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.rubyfood.features.location.shopRevisitStatus.ShopRevisitStatusRepository

object ShopAddStockProvider {
    fun provideShopAddStockRepository(): ShopAddStockRepository {
        return ShopAddStockRepository(ShopAddStockApi.create())
    }
}