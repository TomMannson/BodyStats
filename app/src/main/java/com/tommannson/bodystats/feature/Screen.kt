package com.tommannson.bodystats.feature

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tommannson.bodystats.model.reminding.ReminderType

sealed class Screen(val route: String) {

    object HomeScreen : Screen("home")
    object ConfigurationScreen : Screen("configuration")
    object CreateStatScreen : Screen("create_stats")
    object CreateBodyCompositionScreen : Screen("create_body_composition")
    object PreviewSumaryScreen : Screen("preview_summary_screen")


    object PreviewScreen : Screen("preview_screen") {
        val PARAM_NAME = "statType"
        val routeWithParam get() = "preview_screen/{$PARAM_NAME}"
        fun routeWithStatType() = listOf(navArgument(PARAM_NAME) { type = NavType.StringType })
        fun routeWithParam(statType: String): String {
            return "$route/$statType"
        }
    }

    object CameraScanScreen : Screen("camera_scan")
    object SettingsScreen : Screen("settings_screen")
    object ReminderConfigScreen : Screen("reminder_config_screen") {
        val REMINDER_TYPE = "reminderType"
        val routeWithParam get() = "$route/{$REMINDER_TYPE}"
        fun routeWithReminderType() = listOf(navArgument(REMINDER_TYPE) { type = NavType.StringType })
        fun routeWithParam(reminderType: ReminderType): String {
            return "$route/$reminderType"
        }
    }

}