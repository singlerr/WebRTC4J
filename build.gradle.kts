import com.github.tomtzook.gcmake.targets.CmakeTarget
import org.bytedeco.gradle.javacpp.BuildTask

plugins {
    id("java")
    alias(libs.plugins.gradle.javacpp.build)
    alias(libs.plugins.cmake)
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


val buildPath = projectDir
val installPath = "${buildPath.resolve("webrtc")}"
val cppIncludePath = "$installPath/include"
val apiIncludePath = "$projectDir/src/main/native/webrtc/api"
val cppLibPath = "$installPath/lib"
val buildType = "Release"


tasks.withType<BuildTask> {
    doNotTrackState("")
    includePath = arrayOf(cppIncludePath, apiIncludePath)
    linkPath = arrayOf(cppLibPath)
}


cmake {

    targets {
        create("compileNatives") {
            cmakeLists.set(file("src/main/native/CMakeLists.txt"))
            generatorArgs.add("install")
            cmakeArgs.add("-DWEBRTC_SRC_DIR=$installPath")
            cmakeArgs.add("-DWEBRTC_INSTALL_DIR=${installPath}")
            cmakeArgs.add("-DCMAKE_BUILD_TYPE=${buildType}")
        }
    }
}

tasks.named<BuildTask>("javacppBuildCommand") {
    buildCommand = emptyArray<String>()
    dependsOn("cmakeBuild")
}

tasks.named<BuildTask>("javacppBuildParser") {
    classOrPackageNames = arrayOf("org.bytedeco.webrtc.presets.*")
    outputDirectory = file("${project.layout.buildDirectory.get()}/generated/sources/javacpp")
}

tasks.named<BuildTask>("javacppBuildCompiler") {
    copyLibs = true
}