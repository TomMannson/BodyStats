package com.tommannson.bodystats.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.feature.CreateStatScreen
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.configuration.ConfigurationScreen
import com.tommannson.bodystats.feature.home.HomeDashboardScreen
import com.tommannson.bodystats.feature.previewstats.PreviewStatsScreen
import com.tommannson.bodystats.feature.previewstats.PreviewSummaryScreen
import com.tommannson.bodystats.feature.reminders.ReminderConfig
import com.tommannson.bodystats.feature.reminders.Settings
import com.tommannson.bodystats.model.reminding.ReminderType
import com.tommannson.bodystats.model.statistics.BASIC_PARAMS_FOR_CREATE
import com.tommannson.bodystats.model.statistics.BODY_COMPOSITION_PARAMS

@Composable
fun Navigation(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.HomeScreen.route) {
            HomeDashboardScreen(navController)
        }
        composable(Screen.ConfigurationScreen.route) {
            ConfigurationScreen(navController)
        }
        composable(Screen.CreateStatScreen.route) {
            CreateStatScreen(BASIC_PARAMS_FOR_CREATE, navController)
        }
        composable(Screen.CreateBodyCompositionScreen.route) {
            CreateStatScreen(BODY_COMPOSITION_PARAMS, navController)
        }
        composable(
            Screen.PreviewScreen.routeWithParam,
            arguments = Screen.PreviewScreen.routeWithStatType()
        ) {
            PreviewStatsScreen(
                navController,
                it.arguments?.getString("statType")
            )
        }
        composable(Screen.PreviewScreen.route) {
            PreviewStatsScreen(navController)
        }
        composable(Screen.SettingsScreen.route) {
            Settings(navController)
        }
        composable(
            Screen.ReminderConfigScreen.routeWithParam,
            arguments = Screen.ReminderConfigScreen.routeWithReminderType()
        ) {
            val type = ReminderType.valueOf(
                it.arguments?.getString(Screen.ReminderConfigScreen.REMINDER_TYPE) ?: ""
            )
            ReminderConfig(navController, type)
        }
        composable(
            Screen.PreviewSumaryScreen.route,
        ) {
            PreviewSummaryScreen(navController)
        }
    }
}