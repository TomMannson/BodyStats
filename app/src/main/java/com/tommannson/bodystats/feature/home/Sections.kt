package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.home.HomeState
import com.tommannson.bodystats.feature.home.HomeViewModel

@Composable
fun TopBar(
    onSettings: () -> Unit
) {
    TopAppBar(
        title = { Text("BodyStats") },
        actions = {
            //TODO uncomment when import will be ready
            IconButton(onClick = onSettings) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_settings_24),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun OnBoardSection(onConfigurationOpen: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            Onboard(onConfigurationOpen = onConfigurationOpen)
        }
    }
}

@Composable
fun LoadedData(
    localState: HomeState.DataLoaded,
    viewmodel: HomeViewModel,
    onSettingsButtonClicked: () -> Unit,
    onAddMeasurement: () -> Unit,
    onShowMoreMeasurements: () -> Unit,
    onAddComposition: () -> Unit,
    onParamSelected: (String) -> Unit,
    onSummarySelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        UserInfo(
            localState.currentUser,
            localState.weightInfo,
            onSettingsButtonClicked = onSettingsButtonClicked
        )
        UserWeightInfo(
            localState.weightInfo,
            viewmodel::increaseWeight,
            viewmodel::decreaseWeight
        )
        MyCharts(
            localState.mapOfStats,
            localState.progress,
            onAddClicked = onAddMeasurement,
            onMoreClicked = onShowMoreMeasurements,
            onSummarySelected = onSummarySelected,
            onParamSelected = onParamSelected
            )
        Spacer(modifier = Modifier.height(16.dp))
        NewBodyCompositionStats(
            localState.mapOfStats,
            onAddClicked = onAddComposition
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}