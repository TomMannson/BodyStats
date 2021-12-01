package com.tommannson.bodystats.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tommannson.bodystats.feature.configuration.ScreenState
import com.tommannson.bodystats.feature.createstats.CreateStatsViewmodel
import com.tommannson.bodystats.feature.createstats.sections.StepChangeButtons
import com.tommannson.bodystats.feature.createstats.sections.StepInfo
import com.tommannson.bodystats.feature.createstats.sections.Stepper
import com.tommannson.bodystats.feature.createstats.sections.ValueInput
import com.tommannson.bodystats.ui.controls.Progress


@Composable
fun CreateStatScreen(dataToCreate: List<String>, navController: NavController) {
    val viewModel: CreateStatsViewmodel = hiltViewModel()

    LaunchedEffect(key1 = viewModel) {
        viewModel.initialiseData(dataToCreate, navController)
    }

    val state by viewModel.state.observeAsState()

    if (state == null || state?.viewStateMachine == ScreenState.Init) {
        Progress()
    } else {
        Column(
            Modifier
                .background(MaterialTheme.colors.primary)
        ) {
            Stepper(state, state!!.selectedStep)
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    StepInfo(state)
                    ValueInput(
                        state!!,
                        onValue = viewModel::setValue,
                        onDecrease = viewModel::decreaseCurrent,
                        onIncrease = viewModel::increaseCurrent
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    StepChangeButtons(state, viewModel::goToPrevious, viewModel::goToNext)
                }
            }
        }
    }
}