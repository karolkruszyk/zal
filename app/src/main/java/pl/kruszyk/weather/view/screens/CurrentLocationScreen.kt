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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import pl.kruszyk.weather.view.WeatherSection
import pl.kruszyk.weather.viewmodel.MainViewModel
import pl.kruszyk.weather.viewmodel.STATE

@Composable
fun CurrentLocationScreenWrapper(
    currentLocation: MyLatLng,
    mainViewModel: MainViewModel,
    fetchWeatherInformation: (mainViewModel: MainViewModel, currentLocation: MyLatLng) -> Unit,
    startLocationUpdate: () -> Unit
) {
    CurrentLocationScreen(currentLocation, mainViewModel, fetchWeatherInformation, startLocationUpdate)
}

@Composable
fun ErrorSection(errorMessage: String) {
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, color = Color.White)
    }
}

@Composable
fun LoadingSection() {
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun CurrentLocationScreen(
    currentLocation: MyLatLng,
    mainViewModel: MainViewModel,
    fetchWeatherInformation: (mainViewModel: MainViewModel, currentLocation: MyLatLng) -> Unit,
    startLocationUpdate: () -> Unit
) {
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

    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = true, effect = {
        systemUiController.isSystemBarsVisible = false
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    })

    LaunchedEffect(key1 = true, block = {
        fetchWeatherInformation(mainViewModel, currentLocation)
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
        contentAlignment = Alignment.TopEnd
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val marginTop = screenHeight * 0.1f
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
            when (mainViewModel.state) {
                STATE.LOADING -> {
                    LoadingSection()
                }
                STATE.FAILED -> {
                    ErrorSection(mainViewModel.errorMessage)
                }
                else -> {
                    WeatherSection(mainViewModel.weatherResponse)
                }
            }
        }

        FloatingActionButton(
            onClick = { fetchWeatherInformation(mainViewModel, currentLocation) },
            modifier = Modifier.padding(top = 60.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
        }
    }
}
