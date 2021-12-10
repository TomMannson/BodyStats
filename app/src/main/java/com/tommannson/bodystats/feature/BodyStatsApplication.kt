package com.tommannson.bodystats.feature

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BodyStatsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
        app = this
    }

    companion object {
        lateinit var app: BodyStatsApplication
    }
}