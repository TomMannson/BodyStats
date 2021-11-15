package com.tommannson.bodystats.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.home.sections.*
import com.tommannson.bodystats.infrastructure.configuration.BODY_COMPOSITION_PARAMS
import com.tommannson.bodystats.infrastructure.configuration.FULL_LIST_OF_STATS

@Composable
fun HomeDashboardScreen(navController: NavController) {
    val scafoldState = rememberScaffoldState();
    val viewmodel: HomeViewModel = hiltViewModel()

    LaunchedEffect(key1 = viewmodel) {
        viewmodel.initialiseData(FULL_LIST_OF_STATS)
    }

    val state by viewmodel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BodyStats") },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            painterResource(id = R.drawable.ic_baseline_import_export_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        scaffoldState = scafoldState
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)

        ) {

            val localState = state
            when (localState) {
                is HomeState.DataLoaded -> {
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
                is HomeState.NoData -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            Onboard(navController)
                        }
                    }
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
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