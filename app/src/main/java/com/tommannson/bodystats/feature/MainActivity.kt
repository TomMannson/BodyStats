package com.tommannson.bodystats.feature

//import com.tommannson.bodystats.databinding.ActivityMainBinding
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tommannson.bodystats.feature.home.MainViewModel
import com.tommannson.bodystats.feature.navigation.Navigation
import com.tommannson.bodystats.infrastructure.export.ExportProcessor
import com.tommannson.bodystats.infrastructure.export.PlatformStorageInterface
import com.tommannson.bodystats.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var vm: MainViewModel
    val platformStorageInterface: PlatformStorageInterface = PlatformStorageInterface(this)

    @Inject
    lateinit var exportProcessor: ExportProcessor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Button(this)

        val location = intent.extras?.getString("LINK") ?: Screen.HomeScreen.route

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            ApplicationTheme(useDarkColors = false) {
                Navigation(location)
            }
        }
    }

    fun saveExportLocation() {
        platformStorageInterface.saveDocumentPicker {
            it?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    val stream = contentResolver.openOutputStream(it) as OutputStream
                    exportProcessor.processExport(stream)
                }
            }
        }
    }

    fun loadExportedDataPicker() {
        platformStorageInterface.openDocumentPicker {
            it?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    val stream = contentResolver.openInputStream(it) as InputStream
                    exportProcessor.processImport(stream)
                }
            }
        }
    }
}


