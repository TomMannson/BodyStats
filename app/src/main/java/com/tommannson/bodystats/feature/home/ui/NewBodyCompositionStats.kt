package com.tommannson.bodystats.feature.home.sections

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R
import com.tommannson.bodystats.model.statistics.getStatFormatter
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.infrastructure.SavedStats
import com.tommannson.bodystats.utils.fmt

@Composable
fun NewBodyCompositionStats(
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
                        "Skład ciała",
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
                    Text("Pomiar", modifier = Modifier.weight(.35f))
                    Icon(
                        modifier = Modifier.weight(.2f),
                        painter = painterResource(id = R.drawable.ic_measuremnt_from_24),
                        contentDescription = null
                    )
                    Icon(
                        modifier = Modifier.weight(.2f),
                        painter = painterResource(id = R.drawable.ic_measurement_to_24),
                        contentDescription = null
                    )
                    Icon(
                        modifier = Modifier.weight(.2f),
                        painter = painterResource(id = R.drawable.ic_baseline_compare_arrows_24),
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier.weight(0.05f)
                    )
                }
                Divider()

                Column(
                    modifier = Modifier.padding(horizontal = 0.dp)
                ) {
                    var expandedStat by remember { mutableStateOf(-1) }

                    for (index in 0 until paramsToPresent.size) {
                        val fullItemList = mapOfStats[paramsToPresent[index]] ?: listOf()
                        val samples = fullItemList.takeLast(2)



                        Column(
                            modifier = Modifier.animateContentSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .height(56.dp)
                                    .clickable {
                                        if (expandedStat == index) {
                                            expandedStat = -1
                                            return@clickable
                                        }
                                        expandedStat = index
                                    }
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val paramName =
                                    Configurations.PARAMS_UI_MAP[paramsToPresent[index]]?.shortName
                                        ?: "NO_NAME"


                                Text(
                                    paramName,
                                    modifier = Modifier.weight(.35f),
                                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                                )

                                for (sampleIndex in 0 until 2) {
                                    val valueToPrint =
                                        if (sampleIndex <= samples.size - 1) samples[sampleIndex].value else 0f
                                    Text(
                                        "${valueToPrint fmt getStatFormatter(paramsToPresent[index])}",
                                        modifier = Modifier
                                            .weight(.2f)
                                            .align(Alignment.CenterVertically),
                                        textAlign = TextAlign.Center
                                    )
                                }


                                if (samples.size == 2) {
                                    val lastValue = samples[1].value
                                    val previosValue = samples[0].value

                                    val sign = if (previosValue > lastValue) {
                                        "\u2193 "
                                    } else if (previosValue < lastValue) {
                                        "↑ "
                                    } else {
                                        "  "
                                    }

                                    Text(
                                        "${
                                            Math.abs(previosValue - lastValue) fmt getStatFormatter(
                                                paramsToPresent[index]
                                            )
                                        }${sign}",
                                        modifier = Modifier
                                            .weight(.2f)
                                            .align(Alignment.CenterVertically),
                                        textAlign = TextAlign.End
                                    )
                                } else {
                                    Text(
                                        "0   ",
                                        modifier = Modifier
                                            .weight(.2f)
                                            .align(Alignment.CenterVertically),
                                        textAlign = TextAlign.End
                                    )
                                }

                                val icon = if (expandedStat == index) {
                                    R.drawable.ic_baseline_keyboard_arrow_down_24
                                } else {
                                    R.drawable.ic_baseline_keyboard_arrow_up_24
                                }

                                Icon(
                                    modifier = Modifier.weight(.05f),
                                    painter = painterResource(id = icon),
                                    tint = MaterialTheme.colors.primary,
                                    contentDescription = null
                                )

                            }
                            StatDetail(
                                expand = (expandedStat == index),
                                data = fullItemList.takeLast(10),
                                paramToPresent = paramsToPresent[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatDetail(expand: Boolean, data: List<SavedStats>, paramToPresent: String) {
    Crossfade(targetState = expand) { expanded ->
        if (expanded) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                for (a in 0 until data.size) {
                    val index = (data.size - 1) - a
                    Row(
                        modifier = Modifier.height(48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val paramName =
                            data[index].submitedAt.toString()


                        Text(
                            paramName,
                            modifier = Modifier.weight(.35f),
                        )

                        for (sampleIndex in index - 1 until index + 1) {
                            if (sampleIndex >= 0) {
                                val valueToPrint = data[sampleIndex].value
                                Text(
                                    valueToPrint fmt getStatFormatter(paramToPresent),
                                    modifier = Modifier
                                        .weight(.2f)
                                        .align(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    "-",
                                    modifier = Modifier
                                        .weight(.2f)
                                        .align(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center
                                )
                            }

                        }


                        if (index > 0) {
                            val lastValue = data[index].value
                            val previosValue = data[index - 1].value

                            val sign = if (previosValue > lastValue) {
                                "\u2193 "
                            } else if (previosValue < lastValue) {
                                "↑ "
                            } else {
                                " "
                            }

                            Text(
                                "${
                                    Math.abs(previosValue - lastValue) fmt getStatFormatter(
                                        paramToPresent
                                    )
                                }${sign}",
                                modifier = Modifier
                                    .weight(.2f)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.End
                            )
                        } else {
                            Text(
                                "0 ",
                                modifier = Modifier
                                    .weight(.2f)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.End
                            )
                        }

                        Spacer(modifier = Modifier.weight(.05f))
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier)
        }
    }

}

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}