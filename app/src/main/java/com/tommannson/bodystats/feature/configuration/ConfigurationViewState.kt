package com.tommannson.bodystats.feature.configuration

import androidx.compose.material.DrawerState
import androidx.compose.runtime.*
import com.tommannson.bodystats.infrastructure.ApplicationUser
import com.tommannson.bodystats.infrastructure.Gender
import com.tommannson.bodystats.utils.fmt

data class ConfigurationViewState(
    private val nameState: String,
    private val heightState: String,
    private val weightState: String,
    private val dreamWeightState: String,
    private val dreamGenderState: Int,
) {

    var currentName: String by mutableStateOf(nameState)
        private set

    var currentHeight: String by mutableStateOf(heightState)
        private set

    var currentWeight: String by mutableStateOf(weightState)
        private set

    var currentDreamWeight: String by mutableStateOf(dreamWeightState)
        private set

    var currentDreamGender: Int by mutableStateOf(dreamGenderState)
        private set

    fun editName(name: String) {
        currentName = name
    }

    fun editHeight(height: String) {
        currentHeight = height
    }

    fun editWeight(weight: String) {
        currentWeight = weight
    }

    fun editDreamWeight(weight: String) {
        currentDreamWeight = weight
    }

    fun editGender(gender: Int) {
        currentDreamGender = gender
    }


}


val draw = DrawerState

@Composable
fun rememberConfigurationViewState(
    applicationUser: ApplicationUser?,
): ConfigurationViewState {
    val nameState = applicationUser?.name ?: ""
    val heightState = applicationUser?.height fmt "#"
    val weightState = applicationUser?.weight fmt "#.#"
    val dreamWeightState = applicationUser?.dreamWeight fmt "#.#"
    val dreamGenderState = applicationUser?.sex ?: Gender.MALE
    return remember {
        ConfigurationViewState(
            nameState, heightState, weightState, dreamWeightState, dreamGenderState
        )
    }
}