package com.tommannson.bodystats.feature

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    object HomeScreen : Screen("home")
    object ConfigurationScreen : Screen("configuration")
    object CreateStatScreen : Screen("create_stats")
    object CreateBodyCompositionScreen : Screen("create_body_composition")

    object PreviewScreen : Screen("preview_screen") {
        val PARAM_NAME = "statType"
        val routeWithParam get() = "preview_screen/{$PARAM_NAME}"
        fun routeWithStatType() = listOf(navArgument(PARAM_NAME) { type = NavType.StringType })
        fun routeWithParam(statType: String): String {
            return "$route/$statType"
        }
    }

    object CameraScanScreen : Screen("camera_scan")

}