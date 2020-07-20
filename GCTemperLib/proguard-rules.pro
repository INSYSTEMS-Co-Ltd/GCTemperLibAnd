# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-keep class com.newrelic.** { *; }

-dontwarn com.newrelic.**

-keepattributes Exceptions, Signature, InnerClasses

# apache
-keep class org.apache.http.**
-keep interface org.apache.http.**
-dontwarn org.apache.**
-dontwarn org.apache.**
-dontwarn org.slf4j.**

# realm
-keep @interface io.realm.annotations.RealmModule { *; }
-keep class io.realm.annotations.RealmModule { *; }
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.
-dontwarn io.realm.**
-keepnames public class * extends io.realm.RealmObject
-keep class * extends io.realm.RealmObject

# javax
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.management.**
-dontwarn javax.xml.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn org.conscrypt.*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn okhttp3.internal.platform.*
-dontwarn okhttp3.**

#firebase
-keep class com.google.firebase.**{ *; }
-keep class com.firebase.ui.auth.data.client.AuthUiInitProvider { *; }
-keepattributes Signature