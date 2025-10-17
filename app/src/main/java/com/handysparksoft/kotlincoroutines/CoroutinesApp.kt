package com.handysparksoft.kotlincoroutines

import android.app.Application
import timber.log.Timber

class CoroutinesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
