package com.tommannson.bodystats.feature.configuration

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.configuration.widgets.IconTextField
import com.tommannson.bodystats.feature.configuration.widgets.SenderSelector
import com.tommannson.bodystats.infrastructure.configuration.Gender
import com.tommannson.bodystats.utils.fmt
import kotlinx.coroutines.flow.collect

@Composable
fun ConfigurationScreen(navController: NavHostController) {

    val scafoldState = rememberScaffoldState();

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BodyStats") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
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
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            var nameState by remember {
                mutableStateOf(localState.applicationUser?.name ?: "")
            }
            var heightState by remember {
                mutableStateOf(localState.applicationUser?.height fmt "#")
            }
            var weightState by remember {
                mutableStateOf(localState.applicationUser?.weight fmt "#.#")
            }
            var dreamWeightState by remember {
                mutableStateOf(localState.applicationUser?.dreamWeight fmt "#.#")
            }
            var dreamGenderState by remember {
                mutableStateOf(localState.applicationUser?.sex ?: Gender.MALE)
            }

            val focusManager = LocalFocusManager.current

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    "Przed rozpoczęciem prac potrzebne będzie kilka informacji o Tobie",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(16.dp))


                IconTextField(
                    value = nameState,
                    label = "Imię",
                    iconPainter = painterResource(id = R.drawable.ic_baseline_face_24),
                    onValueChange = {
                        nameState = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                )
                IconTextField(
                    value = heightState,
                    label = "Wzrost (cm)",
                    iconPainter = painterResource(id = R.drawable.ic_baseline_height_24),
                    onValueChange = {
                        heightState = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                )
                IconTextField(
                    value = weightState,
                    label = "Waga (kg)",
                    iconPainter = painterResource(id = R.drawable.ic_baseline_scale_24),
                    onValueChange = {
                        weightState = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                )
                SenderSelector(value = dreamGenderState) {
                    dreamGenderState = it
                }

                Text(
                    "Powiedz nam także, jaka jest Twoja waga marzeń?",
                    modifier = Modifier.padding(16.dp)
                )

                IconTextField(
                    value = dreamWeightState,
                    label = "Waga marzeń (kg)",
                    iconPainter = painterResource(id = R.drawable.ic_baseline_emoji_events_24),
                    onValueChange = {
                        dreamWeightState = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus(force = true)
                        }
                    )
                )

                Spacer(modifier = Modifier.weight(1.0f))

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End),
                    onClick = {

                        viewmodel.submit(
                            name = nameState,
                            height = heightState.replace(",", "."),
                            weight = weightState.replace(",", "."),
                            dreamWeight = dreamWeightState.replace(",", "."),
                            gender = dreamGenderState
                        )
                    }) {
                    Text("Zatwierdź".uppercase())
                }
            }
        }
    }
}