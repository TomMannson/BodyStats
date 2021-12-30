package com.tommannson.bodystats.feature.previewstats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.previewstats.sections.AppBar
import com.tommannson.bodystats.feature.previewstats.sections.SizesProgress
import com.tommannson.bodystats.feature.previewstats.sections.SummaryItem
import com.tommannson.bodystats.model.statistics.BASIC_PARAMS
import com.tommannson.bodystats.model.statistics.Statistic

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreviewSummaryScreen(navController: NavHostController) {

    val listToDisplay: List<String> = mutableListOf(Statistic.SIZE_PROGRESS).also {
        it.addAll(BASIC_PARAMS)
    }
    val scafoldState = rememberScaffoldState();

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
                LoadedContent(localState, onNavigateToDetails = {
                    navController.navigate(Screen.PreviewScreen.routeWithParam(it))
                })
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
    localState: State.DataLoaded,
    onNavigateToDetails: (item: String)-> Unit
) {
    LazyColumn() {
        item {
            Text(
                "Podsumowanie postępów",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
        }
        item {
            SizesProgress(localState.progressInfo)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }


        for (statItem in localState.progressInfo.partialProgress.toList()) {
            item(statItem.first) {
                SummaryItem(statItem.first, statItem.second){
                    onNavigateToDetails(statItem.first)
                }
            }
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        onNavigateToDetails(Statistic.WEIGHT)
                    }) {
                    Text("Więcej")
                }
            }
        }
    }
}


