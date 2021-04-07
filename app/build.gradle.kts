import org.jetbrains.dokka.gradle.DokkaTask
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    id(Plugins.androidApplication)

    id(Plugins.kotlinAndroid)
    // add lint feature
    id(Plugins.detekt)
    // add automatic documentation generator feature
    id(Plugins.dokka)
    // add crash analytics feature
    id(Plugins.fbCrashlytics)
    // add overlaid launcher icons feature
    id(Plugins.ribbonizer)

    id(Plugins.googleServices)
}

val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream("keystore.properties"))

android {
    compileSdkVersion(AndroidSdk.compile)
    defaultConfig {
        applicationId = "es.plexus.android.plexuschuck.application"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        multiDexEnabled = true
    }
    // add version management feature
    generateAppVersioning()
    signingConfigs {
        register("release") {
            storeFile = keystoreProperties["storeFile"]?.let { file(it) }
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    buildTypes {
        named("release").configure {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    sourceSets {
        getByName("main") { java.srcDir("src/main/kotlin") }
    }
    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
        skipEmptyPackages = true // skip empty packages
        skipDeprecated = true // skip deprecated
        noStdlibLink = true // skip documentation related to kotlin-stdlib
    }
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.anko)
    implementation(Libraries.multidex)
    // other modules
    implementation(project(":presentation-layer"))
    implementation(project(":domain-layer"))
    implementation(project(":data-layer"))
    // 3rd party libraries
    implementation(Libraries.koinAndroid)
    implementation(Libraries.fbAnalytics)
    implementation(Libraries.fbCrashlytics)
    detekt(Libraries.detektFormatting)
    detekt(Libraries.detektCli)
    debugImplementation(Libraries.leakCanary)
}

fun generateAppVersioning() {
    val versionPropertiesFile = rootProject.file("version.properties")

    if (versionPropertiesFile.canRead()) {
        val versionProperties = Properties()
        versionProperties.load(FileInputStream(versionPropertiesFile))

        val versionMajor = versionProperties.getProperty("VERSION_MAJOR").toIntOrNull() ?: 0
        val versionMinor = versionProperties.getProperty("VERSION_MINOR").toIntOrNull() ?: 1
        var buildNumber = versionProperties.getProperty("BUILD_NUMBER").toIntOrNull() ?: 1

        android {
            // increment 'buildNumber' in case a release is compiled
            val runTasks = gradle.startParameter.taskNames
            runTasks.forEach { task ->
                if (task.contains("assembleRelease")) {
                    // Update the build number in the local file
                    buildNumber++
                    versionProperties["BUILD_NUMBER"] = (buildNumber).toString()
                    versionProperties.store(FileOutputStream(versionPropertiesFile), null)
                }
            }
            defaultConfig {
                println("Build number: $buildNumber")
                versionCode = buildNumber
                versionName = "$versionMajor.$versionMinor.$buildNumber"
            }
        }
    } else {
        throw GradleException("Could not read 'version.properties' from project root")
    }
}
