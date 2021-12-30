package com.tommannson.bodystats.feature.previewstats.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.model.statistics.getStatFormatter
import com.tommannson.bodystats.model.statistics.getStatUnit
import com.tommannson.bodystats.feature.previewstats.MonthOfYear
import com.tommannson.bodystats.feature.previewstats.WeekScope
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.utils.fmt
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

@Composable
fun HistoryTitle() {
    Text(
        text = "Historia",
        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@Composable
fun SummaryItem(
    name: String,
    value: Float,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            Configurations.PARAMS_UI_MAP[name]?.name ?: "NO_NAME",
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            "${value fmt getStatFormatter(name)} ${
                getStatUnit(
                    name
                )
            }", modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun HistoryRow(
    statItem: SavedStats,
    formatter: DateTimeFormatter?,
    listToDisplay: List<String>,
    page: Int
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            statItem.submitedAt.format(formatter),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            "${statItem.value fmt getStatFormatter(listToDisplay[page])} ${
                getStatUnit(
                    listToDisplay[page]
                )
            }", modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun MonthRow(
    periodInfo: MonthOfYear,
    value: Float,
    listToDisplay: List<String>,
    page: Int
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            periodInfo.monyh.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            "${value fmt getStatFormatter(listToDisplay[page])} ${
                getStatUnit(
                    listToDisplay[page]
                )
            }", modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun WeekRow(
    periodInfo: WeekScope,
    value: Float,
    listToDisplay: List<String>,
    page: Int
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            periodInfo.toString(),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            "${value fmt getStatFormatter(listToDisplay[page])} ${
                getStatUnit(
                    listToDisplay[page]
                )
            }", modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}