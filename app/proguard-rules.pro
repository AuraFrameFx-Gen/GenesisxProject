# Keep important attributes for debugging
-keepattributes SourceFile,LineNumberTable,Signature,*Annotation*,InnerClasses
-renamesourcefileattribute SourceFile

# Kotlin
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Keep Hilt generated code
-keep class * extends dagger.hilt.android.Hilt_* { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.FragmentComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ServiceComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.Dao
-keep class * extends androidx.room.TypeConverter

# Keep Xposed
-keep class de.robv.android.xposed.** { *; }
-keep class dev.aurakai.auraframefx.xposed.** { *; }
-keepnames class dev.aurakai.auraframefx.xposed.**
-keepnames class dev.aurakai.auraframefx.xposed.utils.XPrefs
-keep class dev.aurakai.auraframefx.xposed.** {
    <init>(android.content.Context);
}

# Keep Retrofit
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
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

# EventBus
-keepattributes *Annotation*
-keepclassmembers,allowoptimization,allowobfuscation class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep,allowoptimization,allowobfuscation enum org.greenrobot.eventbus.ThreadMode { *; }

# If using AsyncExecutor, keep required constructor of default event used.
# Adjust the class name if a custom failure event type is used.
-keepclassmembers,allowoptimization,allowobfuscation class org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Accessed via reflection, avoid renaming or removal
-keep,allowoptimization,allowobfuscation class org.greenrobot.eventbus.android.AndroidComponentsImpl

# Keep the ConstraintLayout Motion class
-keep,allowoptimization,allowobfuscation class androidx.constraintlayout.motion.widget.** { *; }

# Keep Recycler View Stuff
-keep,allowoptimization,allowobfuscation class androidx.recyclerview.widget.** { *; }

# Keep Parcelable Creators
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Obfuscation
-repackageclasses
-allowaccessmodification

# Root Service
-keep class dev.aurakai.auraframefx.services.RootProviderProxy { *; }
-keep class dev.aurakai.auraframefx.IRootProviderProxy { *; }

# AIDL Classes
-keep interface **.I* { *; }
-keep class **.I*$Stub { *; }
-keep class **.I*$Stub$Proxy { *; }

# Keep Hilt generated code
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeVisibleTypeAnnotations
-keepattributes Signature
-keepattributes Exceptions

# Keep Hilt generated code for AndroidX
-keepclassmembers class * {
    @dagger.hilt.android.AndroidEntryPoint <init>(...);
}

# Keep Xposed generated code for AndroidX
-keepclassmembers class * {
    @de.robv.android.xposed.IXposedHookLoadPackage <init>(...);
}

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