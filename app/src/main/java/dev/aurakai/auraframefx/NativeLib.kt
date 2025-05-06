package dev.aurakai.auraframefx

class NativeLib {
    /**
     * A native method that is implemented by the 'aura-fx-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'aura-fx-lib' library on application startup.
        init {
            System.loadLibrary("aura-fx-lib")
        }
    }
}
