package xyz.savvamirzoyan.wordremember

import android.app.Application
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.data.database.AppDatabase

class WordRememberApplication : Application() {

    private val debugBuildType = "debug"

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        AppDatabase.getInstance(this)
    }
}