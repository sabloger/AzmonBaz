#EventBus===========================================================================================
-keepclassmembers class ** {public void onEvent(**);}
#Comparable object==================================================================================
-keepclass class * implements java.lang.Comparable{*;}
#Gson===============================================================================================
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class mypersonalclass.data.model.** { *; }
#Zoomable ImageView=================================================================================
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *;}
#Droopy Menu========================================================================================
-dontwarn com.shehabic.droppy.**
-keep class com.shehabic.droppy.** { *;}
-keep interface com.shehabic.droppy.** { *; }
#TestDefinition Object==============================================================================
-keepclassmembers class com.kokabi.p.azmonbaz.Objects.TestDefinitionObj {*;}
#Test Object========================================================================================
-keepclassmembers class com.kokabi.p.azmonbaz.Objects.TestObj {*;}