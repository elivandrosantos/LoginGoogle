# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Google API Client
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}

-keepclassmembers class * {
    @com.google.api.client.util.Key <methods>;
}

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault,EnclosingMethod
-keepattributes *Annotation*

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class ** {
    @com.google.gson.annotations.Expose <fields>;
}
# https://github.com/googleapis/google-api-java-client/issues/1450
-keep public class com.google.api.client.googleapis.GoogleUtils