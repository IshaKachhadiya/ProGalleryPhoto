#include <string.h>
#include <jni.h>


JNIEXPORT jstring JNICALL
Java_com_iten_tenoku_utils_Cpp_baseApi(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "http://192.168.3.119:9000/api/v1/");
}
