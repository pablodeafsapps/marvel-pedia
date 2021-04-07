const val kotlinVersion = "1.4.10"

object Build {
    object Versions {
        const val gradle = "4.0.1"
        const val detekt = "1.15.0-RC1"
        const val dokka = "0.9.17"
        const val ribbonizer = "2.0.0"
        const val fbCrashlyticsGradle = "2.0.0-beta04"
        const val googleServices = "4.2.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val dokkaGradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
    const val ribbonizerPlugin = "com.github.gfx.ribbonizer:ribbonizer-plugin:${Versions.ribbonizer}"
    const val fbCrashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:${Versions.fbCrashlyticsGradle}"
    const val googleServicesPlugin = "com.google.gms:google-services:${Versions.googleServices}"

}

object Plugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val javaLibrary = "java-library"
    const val kotlin = "kotlin"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val dokka = "org.jetbrains.dokka"
    const val fbCrashlytics = "com.google.firebase.crashlytics"
    const val ribbonizer = "com.github.gfx.ribbonizer"
    const val version = "version.gradle.kts"
    const val kotlinKapt = "kotlin-kapt"
    const val googleServices = "com.google.gms.google-services"
}

object AndroidSdk {
    const val min = 21
    const val compile = 29
    const val target = compile
}

// librarias
object Libraries {
    // kotlin
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val arrowCore = "io.arrow-kt:arrow-core:${Versions.arrow}"
    const val arrowSyntax = "io.arrow-kt:arrow-syntax:${Versions.arrow}"
    const val arrowMeta = "io.arrow-kt:arrow-meta:${Versions.arrow}"
    const val anko = "org.jetbrains.anko:anko:${Versions.anko}"
    const val ankoCommons = "org.jetbrains.anko:anko-commons:${Versions.anko}"
    const val ankoDesign = "org.jetbrains.anko:anko-design:${Versions.anko}"
    // androidx
    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val cardview = "androidx.cardview:cardview:${Versions.cardView}"
    // firebase
    const val fbAnalytics = "com.google.firebase:firebase-analytics:${Versions.fbAnalytics}"
    const val fbCrashlytics = "com.google.firebase:firebase-crashlytics:${Versions.fbCrashlytics}"
    const val fbAuth = "com.google.firebase:firebase-auth:${Versions.fbAuth}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    // koin
    const val koinCore = "org.koin:koin-core:${Versions.koin}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinViewmodel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    // retrofit
    const val retrofitCoroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.coroutinesAdapter}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    // moshi
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    // detekt
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Build.Versions.detekt}"
    const val detektCli = "io.gitlab.arturbosch.detekt:detekt-cli:${Build.Versions.detekt}"
    // testing
    const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val junit = "junit:junit:${Versions.junit}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    const val kotlinCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val koinTest = "org.koin:koin-test:${Versions.koin}"
    const val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockitoAndroid}"
    const val testCore = "androidx.test:core-ktx:${Versions.androidxTestCore}"
    const val testRunner = "androidx.test:runner:${Versions.androidxTestRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    private object Versions {
        // core & kotlin
        const val coroutines = "1.3.9"
        const val coroutinesAdapter = "0.9.2"
        // androidx
        const val multidex = "2.0.1"
        const val appCompat = "1.2.0"
        const val lifecycle = "2.3.0-alpha07"
        const val constraintLayout = "2.0.1"
        const val recyclerView = "1.1.0"
        const val cardView = "1.0.0"
        // 3rd party
        const val leakCanary = "2.2"
        const val fbAnalytics = "17.5.0"
        const val fbCrashlytics = "17.2.1"
        const val fbAuth = "19.4.0"
        const val googleServices = "4.3.3"
        const val googleMaterial = "1.1.0-alpha08"
        const val koin = "2.2.0"
        const val arrow = "0.11.0"
        const val retrofit = "2.8.1"
        const val moshi = "1.9.3"
        const val anko = "0.10.8"
        // test
        const val junit = "4.13"
        const val mockitoAndroid = "3.2.4"
        const val mockitoKotlin = "2.1.0"
        const val androidxTestCore = "1.3.0"
        const val androidxTestRunner = "1.3.0"
        const val espresso = "3.3.0"
    }
}