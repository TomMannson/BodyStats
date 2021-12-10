package com.tommannson.bodystats.feature.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.feature.home.sections.LoadedData
import com.tommannson.bodystats.feature.home.sections.Loading
import com.tommannson.bodystats.feature.home.sections.OnBoardSection
import com.tommannson.bodystats.feature.home.sections.TopBar
import com.tommannson.bodystats.model.statistics.FULL_LIST_OF_STATS


@Composable
fun HomeDashboardScreen(navController: NavController) {
    val scafoldState = rememberScaffoldState();
    val viewmodel: HomeViewModel = hiltViewModel()

    LaunchedEffect(key1 = viewmodel) {
        viewmodel.initialiseData(FULL_LIST_OF_STATS)
    }

    val state by viewmodel.state.collectAsState()

    Scaffold(
        topBar = { TopBar() },
        scaffoldState = scafoldState
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)

        ) {
            val localState = state
            Crossfade(targetState = localState) {
                when (it) {
                    is HomeState.DataLoaded -> LoadedData(navController, it, viewmodel)
                    is HomeState.NoData -> OnBoardSection(navController)
                    else -> Loading()
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewHome() {
    HomeDashboardScreen(navController = rememberNavController())
}