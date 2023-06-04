package com.epitome.nashussd.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.epitome.nashussd.services.UssdService.Companion.visibleActivityName

class NashActivityObserver : Application.ActivityLifecycleCallbacks {

    private var tag = "NashActivityObserver"
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        visibleActivityName = activity.localClassName
    }

    override fun onActivityStarted(activity: Activity) {
        visibleActivityName = activity.localClassName
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(tag, "${activity.localClassName} --------------------> is onActivityResumed")
        visibleActivityName = activity.localClassName
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(tag, "${activity.localClassName} --------------------> is onActivityPaused")
        visibleActivityName = ""
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(tag, "${activity.localClassName} --------------------> is onActivityStopped")
        visibleActivityName = ""
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(tag, "${activity.localClassName} --------------------> is onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(tag, "${activity.localClassName} --------------------> is onActivityDestroyed")
        visibleActivityName = ""
    }
}