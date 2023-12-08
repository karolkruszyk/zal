package pl.kruszyk.weather.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.kruszyk.weather.model.MyLatLng
import pl.kruszyk.weather.view.BottomBar
import pl.kruszyk.weather.view.screens.CurrentLocationScreenWrapper
import pl.kruszyk.weather.view.screens.ForecastScreenWrapper
import pl.kruszyk.weather.view.screens.SearchCityScreenWrapper
import pl.kruszyk.weather.viewmodel.MainViewModel

@Composable
fun BottomNavGraph(navController: NavHostController,
                   currentLocation: MyLatLng,
                   mainViewModel: MainViewModel,
                   fetchWeatherInformation: (mainViewModel: MainViewModel, currentLocation: MyLatLng) -> Unit,
                   locationRequired: Boolean,
                   startLocationUpdate: () -> Unit) {
    NavHost(navController = navController, startDestination = BottomBar.CurrentLocation.route) {
        composable(route = BottomBar.Search.route) {
            SearchCityScreenWrapper(
                currentLocation = currentLocation,
                mainViewModel = mainViewModel,
                startLocationUpdate = startLocationUpdate
            )
        }
        composable(route = BottomBar.CurrentLocation.route) {
            CurrentLocationScreenWrapper(
                currentLocation = currentLocation,
                mainViewModel = mainViewModel,
                fetchWeatherInformation = fetchWeatherInformation,
                startLocationUpdate = startLocationUpdate
            )
        }
        composable(route = BottomBar.Forecast.route) {
            ForecastScreenWrapper(
                currentLocation = currentLocation,
                mainViewModel = mainViewModel,
                fetchWeatherInformation = fetchWeatherInformation,
                startLocationUpdate = startLocationUpdate
            )
        }
    }
}