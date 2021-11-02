package com.tommannson.bodystats.feature

//import com.tommannson.bodystats.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tommannson.bodystats.feature.configuration.ConfigurationScreen
import com.tommannson.bodystats.feature.home.HomeDashboardScreen
import com.tommannson.bodystats.feature.home.MainViewModel
import com.tommannson.bodystats.infrastructure.configuration.BASIC_PARAMS
import com.tommannson.bodystats.infrastructure.configuration.BODY_COMPOSITION_PARAMS
import com.tommannson.bodystats.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            ApplicationTheme(useDarkColors = false) {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.HomeScreen.route
                ) {
                    composable(Screen.HomeScreen.route) {
                        HomeDashboardScreen(navController)
                    }
                    composable(Screen.ConfigurationScreen.route) {
                        ConfigurationScreen(navController)
                    }
                    composable(Screen.CreateStatScreen.route) {
                        CreateStatScreen(BASIC_PARAMS)
                    }
                    composable(Screen.CreateBodyCompositionScreen.route) {
                        CreateStatScreen(BODY_COMPOSITION_PARAMS)
                    }
                }
            }
        }
//        vm.prepareScreen()


    }
}


sealed class Screen(val route: String) {

    object HomeScreen : Screen("home")
    object ConfigurationScreen : Screen("configuration")
    object CreateStatScreen : Screen("create_stats")
    object CreateBodyCompositionScreen : Screen("create_body_composition")



}