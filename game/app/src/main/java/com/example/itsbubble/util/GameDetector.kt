package com.example.itsbubble.util

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object GameDetector {
    fun getForegroundPackage(context: Context): String? {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val now = System.currentTimeMillis()
        val events = usm.queryEvents(now - 5000, now)
        var lastPackage: String? = null
        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                lastPackage = event.packageName
            }
        }
        return lastPackage
    }

    fun getInstalledLaunchableApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val mainIntent = android.content.Intent(android.content.Intent.ACTION_MAIN, null)
        mainIntent.addCategory(android.content.Intent.CATEGORY_LAUNCHER)
        val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
        val apps = resolveInfos.map { ri ->
            AppInfo(
                packageName = ri.activityInfo.packageName,
                label = ri.loadLabel(pm).toString(),
                icon = ri.loadIcon(pm)
            )
        }.distinctBy { it.packageName }.sortedBy { it.label }
        return apps
    }

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    data class AppInfo(
        val packageName: String,
        val label: String,
        val icon: android.graphics.drawable.Drawable
    )
}
