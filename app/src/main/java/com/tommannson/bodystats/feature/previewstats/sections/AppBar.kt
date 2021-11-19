package com.tommannson.bodystats.feature.previewstats.sections

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.tommannson.bodystats.R

@Composable
fun AppBar(navController: NavHostController) {
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
}