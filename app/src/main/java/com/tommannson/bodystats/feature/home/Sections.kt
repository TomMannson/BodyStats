package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.home.HomeState
import com.tommannson.bodystats.feature.home.HomeViewModel
import com.tommannson.bodystats.model.statistics.BODY_COMPOSITION_PARAMS

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("BodyStats") },
        actions = {
            //TODO uncomment when import will be ready
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    painterResource(id = R.drawable.ic_baseline_import_export_24),
//                    contentDescription = null
//                )
//            }
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
    UserInfo(navController, localState.currentUser, localState.weightInfo)
    UserWeightInfo(
        localState.weightInfo,
        viewmodel::increaseWeight,
        viewmodel::decreaseWeight
    )
    MyCharts(
        navController,
        localState.mapOfStats,
        onAddClicked = {
            navController.navigate(Screen.CreateStatScreen.route)
        },
        onMoreClicked = {
            navController.navigate(Screen.PreviewScreen.route)
        })
    Spacer(modifier = Modifier.height(16.dp))
    NewBodyCompositionStats(
        BODY_COMPOSITION_PARAMS,
        localState.mapOfStats,
        onAddClicked = {
            navController.navigate(Screen.CreateBodyCompositionScreen.route)
        }
    )
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}