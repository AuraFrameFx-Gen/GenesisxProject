package dev.aurakai.auraframefx.xposed

import android.content.Context
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class AuraXposedEntry : IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            when {
                lpparam.packageName.startsWith("dev.aurakai.auraframefx") -> {
                    handleAppLoad(lpparam)
                }

                lpparam.packageName == "android" || lpparam.packageName == "com.android.systemui" -> {
                    handleFrameworkLoad(lpparam)
                }
            }
        } catch (e: Throwable) {
            Log.e("AuraXposedEntry", "Error in handleLoadPackage: ${e.message}", e)
            throw e
        }
    }

    private fun handleAppLoad(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            Log.i("AuraXposedEntry", "Loaded app package: ${lpparam.packageName}")

            // Hook application initialization
            XposedBridge.hookAllConstructors(
                Class.forName("android.app.Application"),
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val context = param.thisObject as Context
                            setupModuleStatus(context)
                            setupAppLaunchHook(context)
                            setupAmbientMusic(context)
                        } catch (e: Throwable) {
                            Log.e("AuraXposedEntry", "Error in Application hook: ${e.message}", e)
                            throw e
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            Log.e("AuraXposedEntry", "Failed to hook Application: $e")
        }
    }

    private fun handleFrameworkLoad(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            Log.i("AuraXposedEntry", "Loaded framework package: ${lpparam.packageName}")

            // Hook framework initialization
            XposedBridge.hookAllConstructors(
                Class.forName("android.app.ActivityThread"),
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val context = param.thisObject as Context
                            setupFrameworkStatus(context)
                        } catch (e: Throwable) {
                            Log.e("AuraXposedEntry", "Error in Framework hook: ${e.message}", e)
                            throw e
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            Log.e("AuraXposedEntry", "Failed to hook Framework: $e")
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        Log.i("AuraXposedEntry", "Initializing in Zygote")
        // Perform early initialization
    }

    private fun setupModuleStatus(context: Context) {
        try {
            val prefs = context.getSharedPreferences(
                "xposed_status_prefs",
                Context.MODE_PRIVATE
            )
            prefs.edit().apply {
                putBoolean("module_active", true)
                putString("module_version", "1.0")
                putLong("module_timestamp", System.currentTimeMillis())
            }.apply()

            Log.i("AuraXposedEntry", "Module status set successfully")
        } catch (e: Throwable) {
            Log.e("AuraXposedEntry", "Failed to set module status: $e")
        }
    }

    private fun setupFrameworkStatus(context: Context) {
        try {
            val prefs = context.getSharedPreferences(
                "framework_status_prefs",
                Context.MODE_PRIVATE
            )
            prefs.edit().apply {
                putBoolean("framework_active", true)
                putString("framework_version", "35")
                putLong("framework_timestamp", System.currentTimeMillis())
            }.apply()

            Log.i("AuraXposedEntry", "Framework status set successfully")
        } catch (e: Throwable) {
            Log.e("AuraXposedEntry", "Failed to set framework status: $e")
        }
    }
}
