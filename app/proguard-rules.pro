# R8 rules for the release build.
#
# This file shipped as an all-comments template, which is the same as having no
# rules at all — and the build had minify off, so nothing here was ever
# exercised. Both are now on, so what follows is load-bearing.

# ---------------------------------------------------------------------------
# Crash reports
#
# R8 renames everything, so a stack trace from Play arrives as a.b.c(:1) unless
# the line table survives. Keeping it costs a little size and is the difference
# between a readable crash and a useless one. renamesourcefileattribute puts a
# constant in place of the real filename, so nothing about the source layout
# leaks with it.
#
# The mapping file lands at app/build/outputs/mapping/release/mapping.txt and
# travels with an AAB automatically — keep the build that produced a release, or
# Play has nothing to deobfuscate later crashes against.
# ---------------------------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---------------------------------------------------------------------------
# kotlinx.serialization
#
# The compiler plugin generates a $$serializer class and a Companion for every
# @Serializable type. Supabase resolves those reflectively inside decodeList, so
# R8 sees no direct reference and is free to remove them — which fails at
# runtime when a query returns, not at build time where it would be noticed.
#
# The generic rules below are the library's own. The explicit keep on the model
# package follows because there are exactly three data classes in it: a
# whole-class keep is usually a blunt instrument, but across three classes the
# size cost is nil and it removes the entire failure mode, including the
# @SerialName column mappings the Supabase schema depends on.
# ---------------------------------------------------------------------------
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

-keep class com.guide.opbr_companion.data.model.** { *; }
-keep,includedescriptorclasses class com.guide.opbr_companion.data.model.**$$serializer { *; }

# ---------------------------------------------------------------------------
# Ktor and OkHttp
#
# Both ship their own consumer rules; these cover the optional dependencies they
# reference but do not bundle. Without them R8 stops on classes that are never
# reached at runtime.
# ---------------------------------------------------------------------------
-dontwarn org.slf4j.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keepclassmembers class io.ktor.** { volatile <fields>; }

# ---------------------------------------------------------------------------
# Coroutines
#
# The internal ServiceLoader lookups and the volatile state fields are the two
# pieces R8 cannot see referenced.
# ---------------------------------------------------------------------------
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
