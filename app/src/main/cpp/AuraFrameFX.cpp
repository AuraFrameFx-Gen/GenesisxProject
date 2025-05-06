#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "AuraFrameFX_Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring

JNICALL
Java_dev_aurakai_auraframefx_native_NativeBridge_getNativeVersion(
        JNIEnv *env,
        jobject /* this */) {
    std::string version = "1.0.0";
    return env->NewStringUTF(version.c_str());
}

extern "C" JNIEXPORT jboolean

JNICALL
Java_dev_aurakai_auraframefx_native_NativeBridge_initialize(
        JNIEnv *env,
        jobject /* this */) {
    LOGI("Native library initialized");
    return JNI_TRUE;
}

// Register native methods
static JNINativeMethod gMethods[] = {
        {"getNativeVersion", "()Ljava/lang/String;", (void *) Java_dev_aurakai_auraframefx_native_NativeBridge_getNativeVersion},
        {"initialize",       "()Z",                  (void *) Java_dev_aurakai_auraframefx_native_NativeBridge_initialize}
};

JNIEXPORT jint
JNI_OnLoad(JavaVM
* vm,
void *reserved
) {
JNIEnv *env;
if (vm->GetEnv(reinterpret_cast
<void **>(&env), JNI_VERSION_1_6
) != JNI_OK) {
return
JNI_ERR;
}

jclass clazz = env->FindClass("dev/aurakai/auraframefx/native/NativeBridge");
if (clazz == nullptr) {
return
JNI_ERR;
}

if (env->
RegisterNatives(clazz, gMethods,
sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
return
JNI_ERR;
}

return
JNI_VERSION_1_6;
}
