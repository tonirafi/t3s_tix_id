# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
   public void openFileChooser(...);
   public void onShowFileChooser(...);
}
-keepattributes JavascriptInterface
-keepattributes Annotation
-keepclassmembers class * {
@android.webkit.JavascriptInterface <methods>;
}
# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

#app 自有类
#Bean
-keep public class com.tes_tix_id.android.configapp.http.bean.* {*;}
-keepclasseswithmembers class * extends com.tes_tix_id.android.configapp.utils.bean.BaseBean{ *; }

-keep public class com.tes_tix_id.android.android.R$*{
public static final int *;
}
-keep class com.qingsongchou.library.widget.views.*{*;}
-dontwarn com.qingsongchou.library.widget.views.*
-keep public class com.qingsongchou.library.widget.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#cloudinary
-keep class com.cloudinary.** { *; }

#ItemViewProvider
-keep class com.tes_tix_id.android.adapter.** { *; }

#passport
-keep class com.tes_tix_id.android.passport.** { *; }


#AndPermission  proguard-rules
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionYes <methods>;
}
-keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionNo <methods>;
}

#okhttp
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontnote okhttp3.internal.platform.*
-dontwarn okhttp3.internal.platform.AndroidPlatform
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.

#retrofit2  proguard-rules
# Ignore JSR 305 annotations for embedding nullability information.
#-dontwarn javax.annotation.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#如果你的 target API 低于 Android API 27，请添加：
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#zendesk support sdk
-keepattributes InnerClasses
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-dontwarn java.awt.**
-keep,includedescriptorclasses class com.twitter.sdk.**
-keep,includedescriptorclasses class com.squareup.picasso.**
-keep,includedescriptorclasses class io.realm.**
-keep,includedescriptorclasses class okhttp3.**
-keep,includedescriptorclasses class okio.**
-keep,includedescriptorclasses class retrofit2.**
-keep,includedescriptorclasses class com.facebook.**
-keep,includedescriptorclasses class com.aspsine.swipetoloadlayout.**
-keep,includedescriptorclasses class com.google.**
-keep,includedescriptorclasses class javax.inject.Provider
-keep,includedescriptorclasses class com.jakewharton.**
-keep,includedescriptorclasses class com.yalantis.ucrop.**
-keep,includedescriptorclasses class com.walkermanx.photopicker.**
-keep,includedescriptorclasses class com.tes_tix_id.android.socialparty.SocialParty
-keep,includedescriptorclasses class com.tes_tix_id.android.configapp.utils.widget.**
-keep,includedescriptorclasses class com.tes_tix_id.android.configapp.utils.view.animation.AnimationListener
#-keep,includedescriptorclasses class com.qingsongchou.library.widget.**

-dontnote com.facebook.all.All
-dontnote com.android.vending.billing.IInAppBillingService
-dontnote sun.misc.Unsafe
-dontnote cn.bingoogolapple.refreshlayout.**
-dontnote me.relex.circleindicator.CircleIndicator
-dontnote com.google.android.gms.common.api.internal.BasePendingResult$ReleasableResultGuardian
-dontnote com.android.vending.billing.IInAppBillingService



#facebook sdk
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class com.facebook.** {
   *;
}

#twitter sdk
#GSON
# Retain Annotations for model objects
-keepattributes *Annotation*

#Okio
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

-dontwarn javax.annotation.*
-dontwarn javax.annotation.concurrent.*

#realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**

#To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

#Picasso
-keep class com.squareup.picasso.Picasso { *; }
-dontwarn com.squareup.picasso.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------

##---------------Begin: proguard configuration for Crashlytics  ----------
#为了保留所需的信息，以便 Crashlytics 生成人能阅读的崩溃报告，请在您的 Proguard 或 Dexguard 配置文件中添加以下行：
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
#要让 Crashlytics 自动上传 ProGuard 或 DexGuard 映射文件，请从配置文件中移除以下行（若存在）：
#-printmapping mapping.txt
#要使用 ProGuard 更快速地进行编译，可以排除 Crashlytics。 请在您的 ProGuard 配置文件中添加以下行
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# 跳过 AppsFlyer  AFKeystoreWrapper 警告
-keep class com.appsflyer.** { *; }
-dontwarn com.android.installreferrer
