import org.bytedeco.gradle.javacpp.BuildTask

plugins {
    id("java")
    alias(libs.plugins.gradle.javacpp.build)
}

group = "io.github.singlerr"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javacppPlatform: String by project
val targetCpu: String by project
dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    implementation(libs.javacpp)
}

tasks.test {
    useJUnitPlatform()
}

val cppIncludePath = "$buildDir/$javacppPlatform/include"
val cppLibPath = "$buildDir/$javacppPlatform/lib"

tasks.withType<BuildTask> {
    doNotTrackState("")
    includePath = arrayOf(cppIncludePath)
    linkPath = arrayOf(cppLibPath)
}


val copyLibFile by tasks.registering(Copy::class) {
    val srcPath = "$buildDir/${javacppPlatform}/webrtc/src"
    val libPath = "$srcPath/out/$targetCpu/obj"
    val libName = "webrtc.lib"
    from("$libPath/$libName")
    destinationDir = file(cppLibPath)
}

val copyHeaders by tasks.registering(Copy::class) {
    val srcPath = "$buildDir/${javacppPlatform}/webrtc/src"
    include("**/*.h")
    from(srcPath) {
        exclude("src/base/**")
        exclude("src/build/**")
        exclude("src/build_overrides/**")
        exclude("src/buildtools/**")
        exclude("src/data/**")
        exclude("src/docs/**")
        exclude("src/examples/**")
        exclude("src/out/**")
        exclude("src/resources/**")
        exclude("src/sdk/**")
        exclude("src/stats/**")
        exclude("src/style-guide/**")
        exclude("src/test/**")
        exclude("src/testing/**")
        exclude("src/tools/**")
        exclude("src/tools_webrtc/**")
        exclude("test/**")

        filter { line ->
            line.replace("src/third_party/(a[c-z].*|[b-k]|l[a-h|k-z].*|lib[a-x].*|[m-z])", "")
        }

        includeEmptyDirs = false
    }
    into(cppIncludePath)
}

tasks.named<BuildTask>("javacppBuildCommand") {
    buildCommand = arrayOf("cmd", "/c", "build.bat")

    finalizedBy(copyHeaders)
    finalizedBy(copyLibFile)
}

tasks.named<BuildTask>("javacppBuildParser") {
    classOrPackageNames = arrayOf("org.bytedeco.webrtc.presets.*")
    outputDirectory = file("$buildDir/generated/sources/javacpp")
}

tasks.named<BuildTask>("javacppBuildCompiler") {
    copyLibs = true
}