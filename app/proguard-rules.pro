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

# Keep Room entities and DAOs for R8 shrinking
-keep class com.personal.kakeibox.data.entity.** { *; }
-keep class com.personal.kakeibox.data.dao.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.Nonnull