package com.tommannson.bodystats.feature.createstats.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.feature.createstats.State
import com.tommannson.bodystats.feature.createstats.model.Configuration
import com.tommannson.bodystats.feature.createstats.model.Configurations
import com.tommannson.bodystats.infrastructure.Gender
import com.tommannson.bodystats.model.statistics.Statistic

@Preview
@Composable
fun PreviewStepInfo() {
    StepInfo(
        State(0, orderOfItemsToSave = listOf(Statistic.WEIGHT))
    )
}

@Composable
fun StepInfo(
    state: State?
) {
    val config = Configurations.PARAMS_UI_MAP

    Column {
        Image(config[state!!.currentParamKey], state.user?.sex ?: Gender.MALE)
        Name(config[state.currentParamKey]?.name ?: "NO_NAME")
    }
}

@Composable
private fun Name(
    name: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            name,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
private fun Image(
    paramConfig: Configuration?,
    genderInfo: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (paramConfig != null) {
            androidx.compose.foundation.Image(
                modifier = Modifier
                    .align(Alignment.Center),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = paramConfig.genderImage(genderInfo)),
                contentDescription = null,
            )
        }
    }
}