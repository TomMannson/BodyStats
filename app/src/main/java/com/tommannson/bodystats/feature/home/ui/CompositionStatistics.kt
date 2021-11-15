package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.createstats.model.Configurations.PARAMS_UI_MAP
import com.tommannson.bodystats.infrastructure.configuration.SavedStats
import org.threeten.bp.LocalDate

@Composable
fun CompositionStatistics(
    paramsToPresent: List<String>,
    mapOfStats: Map<String, List<SavedStats>>,
    onAddClicked: () -> Unit,
) {
    Column() {
        Card(
            elevation = 3.dp
        ) {
            val firstStats = (mapOfStats[paramsToPresent[1]] ?: listOf())
            var numberOfSamples = firstStats.size

            if (numberOfSamples > 3) {
                numberOfSamples = 3
            }

            Column() {
                Row {
                    Text(
                        "Składu ciała",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    IconButton(onClick = onAddClicked) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_add_circle_24),
                            tint = MaterialTheme.colors.primary, contentDescription = null
                        )
                    }
                }
                Divider()
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Nazwa", modifier = Modifier.weight(.3f))
                    for (sampleNumber in 0 until numberOfSamples) {
                        if (sampleNumber == numberOfSamples - 1) {
                            Text(
                                formatDate(firstStats[sampleNumber].submitedAt),
                                modifier = Modifier.weight(.6f / numberOfSamples),
                                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                formatDate(firstStats[sampleNumber].submitedAt),
                                modifier = Modifier.weight(.6f / numberOfSamples),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Divider()
                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    for (index in 0 until paramsToPresent.size) {
                        val samples = mapOfStats[paramsToPresent[index]]?.takeLast(3) ?: listOf()
                        Row {
                            val paramName = PARAMS_UI_MAP[paramsToPresent[index]]?.name ?: "NO_NAME"


                            Text(paramName, modifier = Modifier.weight(.3f))

                            for (sampleIndex in 0 until samples.size) {
                                var value = samples[sampleIndex].value
                                val sign = if (sampleIndex > 0) {
                                    val previosValue = samples[sampleIndex - 1].value
                                    if (previosValue > value) {
                                        "\u2193 "
                                    } else if (previosValue < value) {
                                        "↑ "
                                    } else {
                                        ""
                                    }
                                } else {
                                    ""
                                }

                                Text(
                                    "${sign}${samples[sampleIndex].value}",
                                    modifier = Modifier
                                        .weight(.6f / samples.size)
                                        .align(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

fun formatDate(date: LocalDate) = "${date.dayOfMonth}:${date.monthValue}"

