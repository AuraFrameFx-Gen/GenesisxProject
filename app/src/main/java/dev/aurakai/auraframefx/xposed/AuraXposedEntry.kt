package dev.aurakai.auraframefx.xposed

// NOTE: This file is ONLY for Xposed/LSPosed builds. It must be excluded or commented out for normal APK builds, otherwise you will get unresolved reference errors.

//import de.robv.android.xposed.IXposedHookLoadPackage
//import de.robv.android.xposed.callbacks.XC_LoadPackage
//import android.util.Log
//
//class AuraXposedEntry : IXposedHookLoadPackage {
//    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
//        Log.i("AuraXposedEntry", "Loaded package: ${lpparam.packageName}")
//        // Set module active flag via shared preferences
//        try {
//            val context = lpparam.appInfo?.let {
//                val clz = Class.forName("android.app.ActivityThread")
//                val currentApplication = clz.getMethod("currentApplication").invoke(null) as? android.app.Application
//                currentApplication
//            }
//            if (context != null) {
//                val prefs = context.getSharedPreferences("xposed_status_prefs", android.content.Context.MODE_PRIVATE)
//                prefs.edit().putBoolean("module_active", true).apply()
//            }
//        } catch (e: Throwable) {
//            Log.e("AuraXposedEntry", "Failed to set module active flag: $e")
//        }
//    }
//}

