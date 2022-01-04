package com.rubyfood.features.weather.api

import com.rubyfood.features.task.api.TaskApi
import com.rubyfood.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}