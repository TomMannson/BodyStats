package com.tommannson.bodystats.feature.configuration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable

import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModelProvider

@Composable
fun ConfigScreen(){

}

@AndroidEntryPoint
class ConfigurationScreen : AppCompatActivity() {

//    val binding: ActivityConfigureBinding by lazy { ActivityConfigureBinding.inflate(layoutInflater) }
    lateinit var vm: ConfigurationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(ConfigurationViewModel::class.java)
//        setContentView(binding.root)

        bindUIDataEvents()
    }

    private fun bindUIDataEvents() {
//        binding.btnSubmit.setOnClickListener(::confirmStatsInfo)
    }

    private fun confirmStatsInfo(view: View?) {
//        binding.requiderContainer.run {
//            val waga = this.etWeight.text.toString()
//            val biust = this.etBiust.text.toString()
//            val talia = this.etTalia.text.toString()
//            val brzuch = this.etBrzuch.text.toString()
//            val biodra = this.etBiodra.text.toString()
//            val udo = this.etUdo.text.toString()
//            val lydka = this.etLydka.text.toString()
//            val ramie = this.etLydka.text.toString()
//            val dataToSent = SavedStatsConfiguration(
//                waga.toFloat(),
//                biust.toFloat(),
//                talia.toFloat(),
//                brzuch.toFloat(),
//                biodra.toFloat(),
//                udo.toFloat(),
//                lydka.toFloat(),
//                ramie.toFloat(),
//                1
//            )
//            vm.submitConfiguration(dataToSent)
//        }
    }

    companion object {

        fun startScreen(context: Context) {
            context.startActivity(Intent(context, ConfigurationScreen::class.java))
        }
    }

}