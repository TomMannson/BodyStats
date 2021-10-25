package com.tommannson.bodystats.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
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
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.home.sections.MyCharts
import com.tommannson.bodystats.feature.home.sections.UserInfo
import com.tommannson.bodystats.feature.home.sections.UserWeightInfo

@Composable
fun HomeDashboardScreen() {
    val scafoldState = rememberScaffoldState();

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
            UserInfo()
            UserWeightInfo()
            ActionButtons()
            MyCharts()
        }
    }
}

@Composable
private fun ColumnScope.ActionButtons() {
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
            onClick = {},
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
            onClick = {},
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
            .clickable { }

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
            ){
                Box(modifier = Modifier.align(Alignment.Center),

                    ){
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
    HomeDashboardScreen()
}
