package com.tommannson.bodystats.feature

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.createstats.CreateStatsViewmodel
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.feature.createstats.textstyling.UnitVisualTransformation
import com.tommannson.bodystats.feature.home.ScreenState
import com.tommannson.bodystats.infrastructure.configuration.BASIC_PARAMS
import com.tommannson.bodystats.ui.theme.ApplicationTheme
import com.tommannson.bodystats.ui.theme.lighPrimary


@Composable
fun CreateStatScreen(dataToCreate: List<String>, navController: NavController) {

    val config = Configurations.PARAMS_UI_MAP
    val viewModel: CreateStatsViewmodel = hiltViewModel()

    LaunchedEffect(key1 = viewModel) {
        viewModel.initialiseData(dataToCreate, navController)
    }

    val state by viewModel.state.observeAsState()

    if (state == null || state?.viewStateMachine == ScreenState.Init) {

    } else {
        Column(
            Modifier
                .background(MaterialTheme.colors.primary)
        ) {
            var currentNumber = state!!.selectedStep
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in 0 until (state?.orderOfItemsToSave?.size ?: 0)) {
                    val selected = item < currentNumber
                    val current = item <= currentNumber

                    val color =
                        if (current) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary.copy(
                            alpha = .0f
                        )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color)
                    ) {
                        val modifier = Modifier
                            .align(Alignment.Center)
                        if (selected) {
                            Icon(
                                tint = MaterialTheme.colors.primary,
                                modifier = modifier,
                                painter = painterResource(id = R.drawable.ic_baseline_check_24),
                                contentDescription = null,
                            )
                        } else {
                            Text(
                                "${item + 1}", style = MaterialTheme.typography.caption.copy(
                                    color = MaterialTheme.colors.lighPrimary
                                ), modifier = modifier
                            )
                        }
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                val currentColor = remember(key1 = currentNumber) { currentNumber }

                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            val localState = state!!
                            val paramConfig = config[localState.currentParamKey]
                            if (paramConfig != null) {
                                Image(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.FillBounds,
                                    painter = painterResource(id = paramConfig.image),
                                    contentDescription = null,
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                config[state!!.currentParamKey]?.name ?: "NO_NAME",
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.h4
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                modifier = Modifier
                                    .padding(start = 32.dp, end = 8.dp)
                                    .size(48.dp)
                                    .clickable(onClick = viewModel::decreaseCurrent),
                                painter = painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
                                contentDescription = null // decorative element
                            )
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {

                                var textForDisplay by remember(state!!.currentValue) {
                                    mutableStateOf(
                                        state!!.currentValueText
                                    )
                                }

                                OutlinedTextField(
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Center
                                    ),
                                    value = textForDisplay,
                                    onValueChange = {
                                        try {
                                            viewModel.setValue(it.replace(",", ".").toFloat())
                                            textForDisplay = it
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
//                                textStyle = MaterialTheme.typography.h4,
                                    modifier = Modifier.align(Alignment.Center),
                                    visualTransformation = UnitVisualTransformation(state!!.paramUnit)
                                )
                            }
                            Icon(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 32.dp)
                                    .size(48.dp)
                                    .clickable(onClick = viewModel::increaseCurrent),
                                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                                contentDescription = null
                            )
                        }

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if((state?.selectedStep ?: 0) != 0 ) {
                                Button(onClick = viewModel::goToPrevious) {
                                    Text("Poprzedni")
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Button(onClick = viewModel::goToNext) {
                                Text(state?.nextButtonText ?: "")
                            }
                        }
                    }
                }

        }
    }


}

@Preview
@Composable
fun PreviewCreateStat() {
    ApplicationTheme(useDarkColors = false) {
        CreateStatScreen(BASIC_PARAMS, rememberNavController())
    }
}