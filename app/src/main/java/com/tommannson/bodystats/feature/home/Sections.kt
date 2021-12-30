package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.*
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
fun OnBoardSection(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            Onboard(navController)
        }
    }
}

@Composable
fun LoadedData(
    navController: NavController,
    localState: HomeState.DataLoaded,
    viewmodel: HomeViewModel
) {
    Column(
    ) {
        UserInfo(navController, localState.currentUser, localState.weightInfo)
        UserWeightInfo(
            localState.weightInfo,
            viewmodel::increaseWeight,
            viewmodel::decreaseWeight
        )
        MyCharts(
            navController,
            localState.mapOfStats,
            localState.progress,
            onAddClicked = {
                navController.navigate(Screen.CreateStatScreen.route)
            },
            onMoreClicked = {
                navController.navigate(Screen.PreviewScreen.route)
            })
        Spacer(modifier = Modifier.height(16.dp))
        NewBodyCompositionStats(
            localState.mapOfStats,
            onAddClicked = {
                navController.navigate(Screen.CreateBodyCompositionScreen.route)
            }
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