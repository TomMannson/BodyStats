package com.tommannson.bodystats.feature.previewstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData2D
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.createstats.getStatFormatter
import com.tommannson.bodystats.feature.createstats.getStatUnit
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.feature.home.ScreenState
import com.tommannson.bodystats.infrastructure.configuration.FULL_LIST_OF_STATS
import com.tommannson.bodystats.utils.fmt
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreviewStatsScreen(navController: NavHostController) {

    val scafoldState = rememberScaffoldState();

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BodyStats") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        scaffoldState = scafoldState
    ) {

        val viewmodel: PreviewStatsViewmodel = hiltViewModel()

        val screenState by viewmodel.state.collectAsState()

        LaunchedEffect(key1 = viewmodel) {
            viewmodel.initPreview()
        }

        val localState = screenState


        if (localState.stateMachine == ScreenState.Init) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            PagableTabLayout(
                FULL_LIST_OF_STATS.size,
                rememberPreviewScreenState(0),
                { index ->
                    Configurations.PARAMS_UI_MAP[FULL_LIST_OF_STATS[index]]?.name ?: "NO_NAME"
                },
            ) { page ->

                val listToDisplay =
                    localState.groupded[FULL_LIST_OF_STATS[page]]?.toMutableList()
                        ?: mutableListOf()

                if (listToDisplay.isEmpty()) {
                    Text("Brak danych do wyświetlenia")
                } else {


                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                val chipsModifier = Modifier.padding(4.dp)

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Yellow)
                                ) {
                                    Text("Miesiące", modifier = chipsModifier)
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Yellow)
                                ) {
                                    Text("Tygodnie", modifier = chipsModifier)
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Yellow)
                                ) {
                                    Text("Dni", modifier = chipsModifier)
                                }
                            }
                        }

                        item {
                            LineChart(
                                lineChartData = LineChartData2D<LocalDate>(
                                    startAtZero = true,
                                    points = listToDisplay.map {
                                        val textToDisplay =
                                            "${it.submitedAt.dayOfMonth} ${it.submitedAt.monthValue}"
                                        LineChartData2D.Point(
                                            it.value,
                                            textToDisplay,
                                            it.submitedAt
                                        )
                                    },
                                    converter = { it.toEpochDay().toFloat() },
                                    displayX = { "${it.dayOfMonth} ${it.monthValue}" },
                                    minX = LocalDate.now().minusDays(10),
                                    maxX = LocalDate.now().plusDays(10),
                                ),

                                // Optional properties.
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .height(300.dp),
                                pointDrawer = FilledCircularPointDrawer(
                                    color = MaterialTheme.colors.primary,
                                    diameter = 8.dp
                                ),
                                lineDrawer = SolidLineDrawer(
                                    color = Color.LightGray,
                                    thickness = 2.dp
                                ),
                                xAxisDrawer = SimpleXAxisDrawer(),
                                yAxisDrawer = SimpleYAxisDrawer(),

                                horizontalOffset = 5f
                            )
                        }

                        item {
                            Text(
                                text = "History",
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        var formatter = DateTimeFormatter.ofPattern("dd MMMM")


                        for (statItem in listToDisplay ?: listOf()) {
                            item(statItem.id) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        statItem.submitedAt.format(formatter),
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        "${statItem.value fmt getStatFormatter(FULL_LIST_OF_STATS[page])} ${
                                            getStatUnit(
                                                FULL_LIST_OF_STATS[page]
                                            )
                                        }", modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}

data class PreviewScreenState
@OptIn(ExperimentalPagerApi::class) constructor(
    val pageState: PagerState,
    var tabIndex: Int,
) {

    @OptIn(ExperimentalPagerApi::class)
    suspend fun selectIndex(indexToSelect: Int) {
        tabIndex = indexToSelect
        pageState.animateScrollToPage(indexToSelect)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPreviewScreenState(initialPage: Int) = remember {
    PreviewScreenState(
        PagerState(initialPage),
        initialPage,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagableTabLayout(
    count: Int,
    tabPagerState: PreviewScreenState,
    titleFactory: (Int) -> String,
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    Column() {
        val coroutineScope = rememberCoroutineScope()
        ScrollableTabRow(selectedTabIndex = tabPagerState.pageState.currentPage) {
            for (i in 0 until count) {
                Tab(
                    selected = i == tabPagerState.tabIndex,
                    text = { Text(titleFactory(i)) },
                    onClick = {
                        coroutineScope.launch {
                            tabPagerState.selectIndex(i)
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = tabPagerState.pageState,
            count = count,
            content = content
        )
    }
}