package pl.kruszyk.weather.network

import pl.kruszyk.weather.constant.Const.Companion.openWeatherMapApiKey
import pl.kruszyk.weather.model.forecast.ForecastResult
import pl.kruszyk.weather.model.weather.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lon: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = openWeatherMapApiKey
    ): WeatherResult

   @GET("forecast")
   suspend fun getForecast(
       @Query("lat") lat: Double = 0.0,
       @Query("lon") lon: Double = 0.0,
       @Query("units") units: String = "metric",
       @Query("appid") appId: String = openWeatherMapApiKey
   ): ForecastResult
}