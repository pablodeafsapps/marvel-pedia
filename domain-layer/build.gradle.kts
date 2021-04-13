import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
    id(Plugins.kotlinKapt)
    // add lint feature
    id(Plugins.detekt)
    // add automatic documentation generator feature
    id(Plugins.dokka)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks {
    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
        skipEmptyPackages = true // skip empty packages
        skipDeprecated = true // skip deprecated
        noStdlibLink = true // skip documentation related to kotlin-stdlib
    }
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.kotlinCoroutinesCore)
    // 3rd party libraries
    implementation(Libraries.koinCore)
    api(Libraries.arrowCore)
    api(Libraries.arrowSyntax)
    kapt(Libraries.arrowMeta)
    detekt(Libraries.detektFormatting)
    detekt(Libraries.detektCli)
    // testing dependencies - Unit Test
    testImplementation(Libraries.junit)
    testImplementation(Libraries.mockitoKotlin)
    testImplementation(Libraries.kotlinCoroutinesTest)
    // koin testing tools
    testImplementation(Libraries.koinTest)
}
