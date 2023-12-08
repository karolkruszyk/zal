package pl.kruszyk.weather.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pl.kruszyk.weather.constant.Const.Companion.cardColor
import pl.kruszyk.weather.constant.Const.Companion.na
import pl.kruszyk.weather.model.forecast.ForecastResult
import pl.kruszyk.weather.utils.Utils.Companion.buildIcon
import pl.kruszyk.weather.utils.Utils.Companion.timestampToHumanDate
import kotlin.math.roundToInt

@Composable
fun ForecastSection(forecastResponse: ForecastResult, isForecastScreen: Boolean = false) {
    return Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecastResponse.list?.let{
            listForecast ->
            if(listForecast!!.size > 0 && !isForecastScreen) {
                LazyRow(modifier = Modifier.fillMaxSize()) {
                    items(listForecast!!) {
                        item ->
                        item.let { _ ->
                            var temp: String
                            var icon: String
                            var time: String

                            item.main.let{main -> temp = if (main == null) na else "${main.temp?.roundToInt()} °C"}
                            item.weather.let{weather -> icon = if (weather == null) na else buildIcon(weather[0].icon!!, isBigSize = false)}
                            item.dt.let{dateTime -> time = if(dateTime == null) na else timestampToHumanDate(dateTime.toLong(), "EEE HH:mm") }

                            ForecastTile(temp = temp, image = icon, time = time)
                        }
                    }
                }
            } else if(listForecast!!.size > 0) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(listForecast!!) {
                            item ->
                        item.let { _ ->
                            var temp: String
                            var icon: String
                            var time: String

                            item.main.let{main -> temp = if (main == null) na else "${main.temp?.roundToInt()} °C"}
                            item.weather.let{weather -> icon = if (weather == null) na else buildIcon(weather[0].icon!!, isBigSize = false)}
                            item.dt.let{dateTime -> time = if(dateTime == null) na else timestampToHumanDate(dateTime.toLong(), "EEE HH:mm") }

                            ForecastTile(temp = temp, image = icon, time = time)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastTile(temp: String, image: String, time: String) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(cardColor).copy(alpha = 0.7f), contentColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(60.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = temp.ifEmpty { na }, color = Color.White)
            AsyncImage(model = image, contentDescription = image, modifier = Modifier.width(50.dp).height(50.dp), contentScale = ContentScale.FillBounds)
            Text(text = time.ifEmpty { na }, color = Color.White)
        }
    }
}
