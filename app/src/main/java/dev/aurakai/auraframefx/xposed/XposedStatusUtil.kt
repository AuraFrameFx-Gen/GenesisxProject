// [Removed by Cascade: redundant file. Use src/main/xposed/XposedStatusUtil.kt for active code.]


import android.content.Context

object XposedStatusUtil {
    private const val PREFS = "xposed_status_prefs"
    private const val KEY_MODULE_ACTIVE = "module_active"

    fun isXposedPresent(): Boolean {
        return try {
            Class.forName("de.robv.android.xposed.XposedBridge")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    fun setModuleActive(context: Context, active: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_MODULE_ACTIVE, active).apply()
    }

    fun isModuleActive(context: Context): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_MODULE_ACTIVE, false)
    }
}
