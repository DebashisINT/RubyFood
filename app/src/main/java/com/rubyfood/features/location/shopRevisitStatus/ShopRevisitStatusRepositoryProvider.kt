package com.rubyfood.features.location.shopRevisitStatus

import com.rubyfood.features.location.shopdurationapi.ShopDurationApi
import com.rubyfood.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}