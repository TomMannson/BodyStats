package com.tommannson.bodystats.feature.home.sections

import androidx.compose.foundation.layout.*
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
    Column(
//        contentPadding = PaddingValues(16.dp)
    ) {
//        items(5, { it }) { index ->
//            if (index == 0) {
        UserInfo(navController, localState.currentUser, localState.weightInfo)
//            } else if (index ==? 1) {
        UserWeightInfo(
            localState.weightInfo,
            viewmodel::increaseWeight,
            viewmodel::decreaseWeight
        )
//            } else if (index == 2) {
        MyCharts(
            navController,
            localState.mapOfStats,
            onAddClicked = {
                navController.navigate(Screen.CreateStatScreen.route)
            },
            onMoreClicked = {
                navController.navigate(Screen.PreviewScreen.route)
            })
//            } else if (index == 3) {
        Spacer(modifier = Modifier.height(16.dp))
//            } else if (index == 4) {
        NewBodyCompositionStats(

            localState.mapOfStats,
            onAddClicked = {
                navController.navigate(Screen.CreateBodyCompositionScreen.route)
            }
        )
//            }
//    }
    }



    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}