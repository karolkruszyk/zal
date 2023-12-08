package pl.kruszyk.weather.view.screens

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.coroutineScope
import pl.kruszyk.weather.constant.Const.Companion.colorBg1
import pl.kruszyk.weather.constant.Const.Companion.colorBg2
import pl.kruszyk.weather.constant.Const.Companion.permissions
import pl.kruszyk.weather.model.MyLatLng
import pl.kruszyk.weather.view.ForecastSection
import pl.kruszyk.weather.view.WeatherSection
import pl.kruszyk.weather.viewmodel.MainViewModel
import pl.kruszyk.weather.viewmodel.STATE
@Composable
fun SearchCityScreenWrapper(
    currentLocation: MyLatLng,
    mainViewModel: MainViewModel,
    startLocationUpdate: () -> Unit
) {
    SearchCityScreen(currentLocation, mainViewModel, startLocationUpdate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchCityScreen(
    currentLocation: MyLatLng,
    mainViewModel: MainViewModel,
    startLocationUpdate: () -> Unit
) {
    var selectedCity by remember { mutableStateOf("") }
    val context = LocalContext.current
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->
        val areGranted = permissionMap.values.reduce { accepted, next -> accepted && next }

        if (areGranted) {
            startLocationUpdate()
            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchWeatherInformation(mainViewModel: MainViewModel) {
        mainViewModel.state = STATE.LOADING
        mainViewModel.getWeatherByCity(selectedCity)
        mainViewModel.state = STATE.SUCCESS
    }

    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = true, effect = {
        systemUiController.isSystemBarsVisible = false
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    })

    LaunchedEffect(key1 = selectedCity, block = {
        fetchWeatherInformation(mainViewModel)
    })

    LaunchedEffect(key1 = currentLocation, block = {
        coroutineScope {
            if (permissions.all {
                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }) {
                startLocationUpdate()
            } else {
                launcherMultiplePermissions.launch(permissions)
            }
        }
    })

    val gradient = Brush.linearGradient(
        colors = listOf(Color(colorBg1), Color(colorBg2)),
        start = Offset(1000f, -1000f),
        end = Offset(1000f, 1000f)
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.TopCenter
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val marginTop = screenHeight * 0.02f
        val marginTopPx = with(LocalDensity.current) { marginTop.toPx() }


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)

                    layout(placeable.width, placeable.height + marginTopPx.toInt()) {
                        placeable.placeRelative(0, marginTopPx.toInt())
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = selectedCity,
                onValueChange = { selectedCity = it },
                label = { Text("City") }
            )


            when (mainViewModel.state) {
                STATE.LOADING -> {

                    LoadingSection()
                }
                STATE.FAILED -> {

                    Text("Please enter the city name")
                }
                else -> {

                    WeatherSection(mainViewModel.weatherResponse)
                    ForecastSection(mainViewModel.forecastResponse)
                }
            }

        }
    }
}
