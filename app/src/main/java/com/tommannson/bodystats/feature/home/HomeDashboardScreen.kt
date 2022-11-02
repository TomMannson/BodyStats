package com.tommannson.bodystats.feature.home

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tommannson.bodystats.feature.MainActivity
import com.tommannson.bodystats.feature.home.sections.LoadedData
import com.tommannson.bodystats.feature.home.sections.Loading
import com.tommannson.bodystats.feature.home.sections.OnBoardSection
import com.tommannson.bodystats.feature.home.sections.TopBar
import com.tommannson.bodystats.model.statistics.FULL_LIST_OF_STATS


@Composable
fun HomeDashboardScreen(
    viewModel: HomeViewModel,
    onUserInfoOpen: () -> Unit,
    onSettingsOpen: () -> Unit,
    onAddMeasurements: () -> Unit,
    onShowMoreMeasurements: () -> Unit,
    onAddComposition: () -> Unit,
    onParamSelected: (String) -> Unit,
    onSummarySelected: () -> Unit
) {
    val scafoldState = rememberScaffoldState();

    LaunchedEffect(key1 = viewModel) {
        viewModel.initialiseData(FULL_LIST_OF_STATS)
    }

    val state by viewModel.state.collectAsState()
    state.toString()

    Scaffold(
        topBar = { TopBar(onSettings = onSettingsOpen) },
        scaffoldState = scafoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            val localState = state

            if (localState is HomeState.Loading) {
                Loading()
            } else {
                Crossfade(targetState = localState is HomeState.DataLoaded) { dataLoaded ->
                    if (dataLoaded) {
                        LoadedData(
                            localState as HomeState.DataLoaded,
                            viewModel,
                            onUserInfoOpen,
                            onAddMeasurement = onAddMeasurements,
                            onAddComposition = onAddComposition,
                            onShowMoreMeasurements = onShowMoreMeasurements,
                            onSummarySelected = onSummarySelected,
                            onParamSelected = onParamSelected
                        )
                    } else {
                        OnBoardSection(onConfigurationOpen = onUserInfoOpen)
                    }
                }
            }


        }
    }
}

fun openFilePicker(ctx: Context) {
    val screen = ctx as MainActivity
    screen.saveExportLocation()
}


@Preview
@Composable
fun PreviewHome() {
    HomeDashboardScreen(
        viewModel = hiltViewModel(),
        onUserInfoOpen = {},
        onSettingsOpen = {},
        onAddMeasurements = {},
        onShowMoreMeasurements = {},
        onAddComposition = {},
        onSummarySelected = {},
        onParamSelected = {}
    )
}