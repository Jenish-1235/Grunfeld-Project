# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview ***REMOVED***
#   public *;
#***REMOVED***

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> ***REMOVED***
   static <1>$Companion Companion;
***REMOVED***

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** ***REMOVED***
   static **$* *;
***REMOVED***
-keepclassmembers class <2>$<3> ***REMOVED***
   kotlinx.serialization.KSerializer serializer(...);
***REMOVED***

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** ***REMOVED***
   public static ** INSTANCE;
***REMOVED***
-keepclassmembers class <1> ***REMOVED***
   public static <1> INSTANCE;
   kotlinx.serialization.KSerializer serializer(...);
***REMOVED***
-keep class com.android.grunfeld_project.models.User$$serializer ***REMOVED*** *; ***REMOVED***

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault