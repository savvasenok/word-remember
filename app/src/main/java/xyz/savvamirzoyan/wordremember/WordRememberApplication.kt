package xyz.savvamirzoyan.wordremember

import android.app.Application
import timber.log.Timber
import xyz.savvamirzoyan.wordremember.data.database.AppDatabase

class WordRememberApplication : Application() {

    private val debugBuildType = "debug"

    private class ProductionDebugTree : Timber.Tree() {
        override fun v(message: String?, vararg args: Any?) {}
        override fun v(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun v(t: Throwable?) {}
        override fun d(message: String?, vararg args: Any?) {}
        override fun d(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun d(t: Throwable?) {}
        override fun i(message: String?, vararg args: Any?) {}
        override fun i(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun i(t: Throwable?) {}
        override fun w(message: String?, vararg args: Any?) {}
        override fun w(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun w(t: Throwable?) {}
        override fun e(message: String?, vararg args: Any?) {}
        override fun e(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun e(t: Throwable?) {}
        override fun wtf(message: String?, vararg args: Any?) {}
        override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {}
        override fun wtf(t: Throwable?) {}
        override fun log(priority: Int, message: String?, vararg args: Any?) {}
        override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {}
        override fun log(priority: Int, t: Throwable?) {}
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
        override fun isLoggable(priority: Int): Boolean = false
        override fun isLoggable(tag: String?, priority: Int): Boolean = false
        override fun formatMessage(message: String, args: Array<out Any>): String = ""
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.BUILD_TYPE == debugBuildType) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ProductionDebugTree())
        }

        AppDatabase.getInstance(this)
    }
}