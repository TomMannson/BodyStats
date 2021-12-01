package com.tommannson.bodystats.feature.configuration

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tommannson.bodystats.feature.configuration.widgets.*
import com.tommannson.bodystats.ui.controls.Progress
import kotlinx.coroutines.flow.collect

@Composable
fun ConfigurationScreen(navController: NavHostController) {

    val scafoldState = rememberScaffoldState();

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        scaffoldState = scafoldState
    ) {

        val viewmodel: ConfigurationViewModel = hiltViewModel()
        val context = LocalContext.current
        val screenState by viewmodel.configurationState.observeAsState()

        LaunchedEffect(key1 = viewmodel) {
            viewmodel.initializeScreen()
            viewmodel.events.collect {
                when (it) {
                    is Events.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()
                    is Events.Created -> navController.popBackStack()
                }
            }
        }

        val localState = screenState

        if (localState == null) {
            Progress()
        } else {
            UserForm(localState, onSubmit = { configState ->
                viewmodel.submit(
                    name = configState.currentName,
                    height = configState.currentHeight.replace(",", "."),
                    weight = configState.currentWeight.replace(",", "."),
                    dreamWeight = configState.currentDreamWeight.replace(",", "."),
                    gender = configState.currentDreamGender
                )
            })
        }
    }
}

@Composable
private fun UserForm(
    localState: ConfigurationState,
    onSubmit: (ConfigurationViewState) -> Unit
) {
    val configState = rememberConfigurationViewState(localState.applicationUser)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Przed rozpoczęciem prac potrzebne będzie kilka informacji o Tobie",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))


        NameField(configState.currentName, onChanged = configState::editName)
        HeightField(
            configState.currentHeight,
            onChanged = configState::editHeight,
        )
        WeightField(
            configState.currentWeight,
            onChanged = configState::editWeight
        )
        GenderSelector(
            value = configState.currentDreamGender,
            onGenderSelected = configState::editGender,
        )

        Text(
            "Powiedz nam także, jaka jest Twoja waga marzeń?",
            modifier = Modifier.padding(16.dp)
        )

        DreamWeightField(
            configState.currentDreamWeight,
            focusManager,
            onValueChange = configState::editDreamWeight,
        )

        localState.applicationUser?.let {
            TextButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
                Text("Zaplanuj przypomnienia".uppercase())
            }
        }

        Spacer(modifier = Modifier.weight(1.0f))

        SubmitButton(modifier = Modifier.align(Alignment.End), onSubmit = {
            onSubmit(configState)
        })
    }
}

