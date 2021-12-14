package com.tommannson.bodystats.feature.configuration.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.tommannson.bodystats.R

@Composable
fun TopBar(navController: NavController, title: String = "BodyStats") {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = buildIconInNeeded(navController)
    )
}

fun buildIconInNeeded(navController: NavController): @Composable (() -> Unit)? {
    if (navController.previousBackStackEntry != null)
        return {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = null
                )
            }
        }
    else {
        return null
    }
}
