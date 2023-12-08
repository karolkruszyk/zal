package pl.kruszyk.weather.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar (
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Search : BottomBar(
        route = "searchCity",
        title = "Search City",
        icon = Icons.Default.Search
    )

    object CurrentLocation : BottomBar(
        route = "currentLocation",
        title = "Weather",
        icon = Icons.Default.LocationOn
    )

    object Forecast : BottomBar(
        route = "forecast",
        title = "Forecast",
        icon = Icons.Default.DateRange
    )

}