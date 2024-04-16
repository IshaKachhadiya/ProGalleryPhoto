
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#
#-keepattributes SourceFile,LineNumberTable
#
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class hdphoto.galleryimages.gelleryalbum.model.** { *; }

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**

# Ucrop
-keep class com.yalantis.ucrop.** { *; }

# Photoeditor
-keep class ja.burhanrashid52.photoeditor.** { *; }

# Android Image Cropper
-keep class com.theartofdev.edmodo.cropper.** { *; }

# Gesture Views
-keep class com.alexvasilkov.gestures.** { *; }

# HttpClient
-keep class cz.msebera.android.httpclient.** { *; }

# Glide
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

# RxAndroid
-keep class io.reactivex.android.schedulers.AndroidSchedulers { *; }
-keep class io.reactivex.schedulers.Schedulers { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature

# OkHttp Logging Interceptor
-keep class okhttp3.logging.** { *; }

# Gson Converter
-keepattributes Signature
-keep class com.google.gson.** { *; }

# Commons IO
-keep class org.apache.commons.io.** { *; }

# Core KTX
-keep class androidx.core.** { *; }

# Lifecycle
-keep class androidx.lifecycle.** { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.j2objc.annotations.ReflectionSupport$Level
-dontwarn com.google.j2objc.annotations.ReflectionSupport
-dontwarn com.google.j2objc.annotations.RetainedWith
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
