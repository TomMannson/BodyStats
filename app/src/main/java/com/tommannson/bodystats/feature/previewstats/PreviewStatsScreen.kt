package com.tommannson.bodystats.feature.previewstats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.feature.previewstats.sections.*
import com.tommannson.bodystats.feature.previewstats.ui.PagableTabLayout
import com.tommannson.bodystats.feature.previewstats.ui.rememberPreviewScreenState
import com.tommannson.bodystats.infrastructure.configuration.BASIC_PARAMS
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreviewStatsScreen(navController: NavHostController, itemToSelect: String? = null) {

    val listToDisplay: List<String> = BASIC_PARAMS
    val scafoldState = rememberScaffoldState();
    val selectedPage = itemToSelect?.let { listToDisplay.indexOf(itemToSelect) } ?: 0

    Scaffold(
        topBar = {
            AppBar(navController)
        },
        scaffoldState = scafoldState
    ) {

        val viewmodel: PreviewStatsViewmodel = hiltViewModel()
        val screenState by viewmodel.state.collectAsState()

        LaunchedEffect(key1 = viewmodel) {
            viewmodel.initPreview(listToDisplay)
        }

        val localState = screenState


        when (localState) {
            is State.DataLoaded -> {
                LoadedContent(listToDisplay, selectedPage, localState, viewmodel)
            }
            else -> {
                LoadingProgress()
            }
        }
    }
}

@Composable
private fun LoadingProgress() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun LoadedContent(
    listToDisplay: List<String>,
    selectedPage: Int,
    localState: State.DataLoaded,
    viewmodel: PreviewStatsViewmodel
) {
    var selectedKind = remember { mutableStateOf(TimeKind.DAYLY) }
//    val LocalView.current


    PagableTabLayout(
        listToDisplay.size,
        rememberPreviewScreenState(selectedPage),
        { index ->
            Configurations.PARAMS_UI_MAP[listToDisplay[index]]?.name ?: "NO_NAME"
        },
        unPageableContent = {
            TimeUnitChipsSelector(selectedKind, onSelected = { viewmodel.selectChart(it) })
        }
    ) { page ->
        val listOfItems =
            localState.groupded[listToDisplay[page]]?.toMutableList()
                ?: mutableListOf()

        CreateGrid(localState.allCalculatedWariants[TimeKind.MONTHLY]!!)

        if (listOfItems.isEmpty()) {
            Text("Brak danych do wyświetlenia")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val graphPresentationMethod = localState.selectedMethod
                item {
                    when (graphPresentationMethod) {
                        is StatsShowInTime.DailyStats -> {
                            Text("W ciągu ostatnich 60 dni",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                                )
                        }

                        is StatsShowInTime.WeaklyStats -> {
                            Text("W ciągu ostatnich 6 miesięcy",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                            )
                        }
                        is StatsShowInTime.MonthlyStats -> {
                            Text("W ciągu ostatnich 2 lat",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                            )
                        }
                    }
                }
                item {

                    when (graphPresentationMethod) {
                        is StatsShowInTime.DailyStats -> {
                            val items =
                                graphPresentationMethod.groupded[listToDisplay[page]]?.toMutableList()
                                    ?: mutableListOf()
                            DayChart(items)
                        }
                        is StatsShowInTime.MonthlyStats -> {
                            val items =
                                graphPresentationMethod.gridInfo[listToDisplay[page]]!!
                            MonthGrid(items)
                        }
                        is StatsShowInTime.WeaklyStats -> {
                            val items =
                                graphPresentationMethod.gridInfo[listToDisplay[page]]!!
                            WeekGrid(items)
                        }
                    }

                }

                var formatter = DateTimeFormatter.ofPattern("dd MMMM")
                item {
                    HistoryTitle()
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                when (graphPresentationMethod) {
                    is StatsShowInTime.DailyStats -> {
                        for (statItem in listOfItems.reversed()) {
                            item(statItem.id) {
                                HistoryRow(statItem, formatter, listToDisplay, page)
                            }
                        }
                    }
                    is StatsShowInTime.MonthlyStats -> {
                        val items =
                            graphPresentationMethod.gridInfo[listToDisplay[page]]!!
                        for (statItem in items.collectionOfMonths) {
                            item(statItem.first.toString()) {
                                MonthRow(
                                    statItem.first,
                                    statItem.second,
                                    listToDisplay,
                                    page
                                )
                            }
                        }
                    }
                    is StatsShowInTime.WeaklyStats -> {
                        val items =
                            graphPresentationMethod.gridInfo[listToDisplay[page]]!!
                        for (statItem in items.collectionOfWeeks) {
                            item(statItem.first.toString()) {
                                WeekRow(
                                    statItem.first,
                                    statItem.second,
                                    listToDisplay,
                                    page
                                )
                            }
                        }
                    }

                }


            }
        }
    }
}

@Composable
fun CreateGrid(statsShowInTime: StatsShowInTime) {

}
