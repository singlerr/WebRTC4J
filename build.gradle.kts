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
val installPath = "${layout.buildDirectory.file("webrtc").get()}"
val cppIncludePath = "$installPath/include"
val apiIncludePath = "src/main/native/webrtc/api"
val cppLibPath = "$installPath/lib"



tasks.withType<BuildTask> {
    doNotTrackState("")
    includePath = arrayOf(cppIncludePath, apiIncludePath)
    linkPath = arrayOf(cppLibPath)
}


cmake {
    targets {
        create("compileNatives") {
            cmakeLists.set(file("src/main/native/CMakeLists.txt"))
            cmakeArgs.add("-DWEBRTC_SRC_DIR=$installPath")
            cmakeArgs.add("-DWEBRTC_INSTALL_DIR=${installPath}")
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