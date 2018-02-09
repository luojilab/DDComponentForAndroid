# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/klee/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-ignorewarnings
-dontoptimize
-keep class !android.support.v7.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.** { *; }
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.support.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends com.netease.ntespmmvp.** {*;}

-keep enum * { *;}

-keep interface * {
  <methods>;
}
-keep class com.luojilab.component.componentlib.** {*;}
-keep class com.luojilab.router.** {*;}
-keep class com.luojilab.gen.** {*;}
-keep class * implements com.luojilab.component.componentlib.router.ISyringe {*;}
-keep class * implements com.luojilab.component.componentlib.applicationlike.IApplicationLike {*;}
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.**
