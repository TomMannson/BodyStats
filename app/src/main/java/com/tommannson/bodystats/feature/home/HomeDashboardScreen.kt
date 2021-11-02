package com.tommannson.bodystats.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.home.sections.MyCharts
import com.tommannson.bodystats.feature.home.sections.Onboard
import com.tommannson.bodystats.feature.home.sections.UserInfo
import com.tommannson.bodystats.feature.home.sections.UserWeightInfo
import com.tommannson.bodystats.infrastructure.configuration.BASIC_PARAMS
import com.tommannson.bodystats.infrastructure.configuration.FULL_LIST_OF_STATS

@Composable
fun HomeDashboardScreen(navController: NavController) {
    val scafoldState = rememberScaffoldState();
    val viewmodel: HomeViewModel = hiltViewModel()

    LaunchedEffect(key1 = viewmodel) {
        viewmodel.initialiseData(FULL_LIST_OF_STATS)
    }

    val state = viewmodel.state.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BodyStats") }
            )
        },
        floatingActionButton = {

        },
        scaffoldState = scafoldState
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)

        ) {

            val localState = state.value

            if (localState == null || localState.screenState == ScreenState.Loading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (localState.currentUser == null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Onboard(navController)
                    }
                }
            } else {
                UserInfo(navController, localState.currentUser)
                UserWeightInfo(localState.weightInfo, viewmodel::increaseWeight, viewmodel::decreaseWeight)
                ActionButtons(
                    onAddMeassurment = {
                        navController.navigate(Screen.CreateStatScreen.route)
                    },
                    onAddBodyComposition = {
                        navController.navigate(Screen.CreateBodyCompositionScreen.route)
                    }
                )
                MyCharts(localState.mapOfStats)
            }
        }
    }
}

@Composable
private fun ColumnScope.ActionButtons(
    onAddMeassurment: () -> Unit,
    onAddBodyComposition: () -> Unit
) {
    Row(
        modifier = Modifier.Companion.align(
            Alignment.CenterHorizontally
        )
    ) {
        ActionButton(
            text = "Dodaj Pomiar",
            icon = {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    contentDescription = null
                )
            },
            onClick = onAddMeassurment,
        )
        ActionButton(
            text = "Dodaj skład ciała",
            icon = {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    contentDescription = null
                )
            },
            onClick = onAddBodyComposition,
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    iconColor: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
            )
            .clickable(onClick = onClick)

    ) {
        Column(
            Modifier
                .padding(8.dp)
                .requiredWidth(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(iconColor),
            ) {
                Box(
                    modifier = Modifier.align(Alignment.Center),

                    ) {
                    icon()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    HomeDashboardScreen(navController = rememberNavController())
}