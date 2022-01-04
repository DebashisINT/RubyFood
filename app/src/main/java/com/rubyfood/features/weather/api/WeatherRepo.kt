package com.rubyfood.features.weather.api

import com.rubyfood.base.BaseResponse
import com.rubyfood.features.task.api.TaskApi
import com.rubyfood.features.task.model.AddTaskInputModel
import com.rubyfood.features.weather.model.ForeCastAPIResponse
import com.rubyfood.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}