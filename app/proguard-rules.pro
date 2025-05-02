# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle

# Keep Hilt classes
-keep class dagger.* { *; }
-keep class * extends dagger.* { *; }
-keep class * extends dagger.internal.* { *; }
-keep class * extends dagger.internal.codegen.* { *; }
-keep class * extends dagger.internal.codegen.binding.* { *; }
-keep class * extends dagger.internal.codegen.component.* { *; }
-keep class * extends dagger.internal.codegen.model.* { *; }
-keep class * extends dagger.internal.codegen.writing.* { *; }

# Keep Compose classes
-keep class androidx.compose.runtime.* { *; }
-keep class androidx.compose.ui.* { *; }
-keep class androidx.compose.foundation.* { *; }
-keep class androidx.compose.material.* { *; }
-keep class androidx.compose.material3.* { *; }

# Keep Xposed classes
-keep class de.robv.android.xposed.* { *; }
-keep class de.robv.android.xposed.XposedBridge { *; }
-keep class de.robv.android.xposed.XposedHelpers { *; }

# Keep ViewModel classes
-keep class androidx.lifecycle.* { *; }
-keep class androidx.lifecycle.ViewModel { *; }

# Keep Hilt ViewModel injection
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep navigation classes
-keep class androidx.navigation.* { *; }
-keep class androidx.navigation.fragment.* { *; }
-keep class androidx.navigation.ui.* { *; }

# Keep Coroutines classes
-keep class kotlinx.coroutines.* { *; }
-keep class kotlinx.coroutines.flow.* { *; }

# Keep Room classes
-keep class androidx.room.* { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.RoomDatabase$Builder { *; }

# Keep Retrofit classes
-keep class retrofit2.* { *; }
-keep class okhttp3.* { *; }

# Keep OkHttp classes
-keep class okhttp3.* { *; }
-keep interface okhttp3.* { *; }

# Keep Gson classes
-keep class com.google.gson.* { *; }
-keep class com.google.gson.stream.* { *; }

# Keep Hilt generated classes
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeVisibleTypeAnnotations
-keepattributes Signature
-keepattributes Exceptions

# Keep Hilt generated code
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# Keep Xposed generated code
-keepclassmembers class * {
    @de.robv.android.xposed.XposedBridge <init>(...);
}

# Keep Compose generated code
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <init>(...);
}

# Keep navigation generated code
-keepclassmembers class * {
    @androidx.navigation.NavGraph <init>(...);
}

# Keep ViewModel generated code
-keepclassmembers class * {
    @androidx.lifecycle.ViewModel <init>(...);
}

# Keep Room generated code
-keepclassmembers class * {
    @androidx.room.Entity <init>(...);
}

# Keep Retrofit generated code
-keepclassmembers class * {
    @retrofit2.http.GET <init>(...);KL;
}

# Keep OkHttp generated code
-keepclassmembers class * {
    @okhttp3.internal.annotations.EverythingIsNonNull <init>(...);
}

# Keep Gson generated code
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <init>(...);
}

# Keep Hilt generated code for AndroidX
-keepclassmembers class * {
    @dagger.hilt.android.AndroidEntryPoint <init>(...);
}

# Keep Xposed generated code for AndroidX
-keepclassmembers class * {
    @de.robv.android.xposed.IXposedHookLoadPackage <init>(...);
}