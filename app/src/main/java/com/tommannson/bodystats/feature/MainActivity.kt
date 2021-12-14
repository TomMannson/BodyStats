package com.tommannson.bodystats.feature

//import com.tommannson.bodystats.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.tommannson.bodystats.feature.home.MainViewModel
import com.tommannson.bodystats.feature.navigation.Navigation
import com.tommannson.bodystats.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = intent.extras?.getString("LINK" ) ?: Screen.HomeScreen.route

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            ApplicationTheme(useDarkColors = false) {
                Navigation(location)
            }
        }
    }
}


