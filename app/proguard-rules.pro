# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.example.theotokos.* {
   <init>(*);
}

# Keep MainActivity and related classes
-keep public class com.example.theotokos.MainActivity { *; }
-keep public class com.example.theotokos.ViewPagerAdapter { *; }
-keep interface com.example.theotokos.ViewPagerAdapter$OnAllImagesLoadedListener { *; }
-keep class com.example.theotokos.DataCache { *; }

# Android views and activities
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# AndroidX
-keep class androidx.** { *; }

# Menu items
-keepclassmembers class * {
    @android.view.MenuItem <fields>;
}
#-keep class com.example.theotokos.** {*;}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile