import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.commons.io.FilenameUtils
import org.gradle.kotlin.dsl.support.unzipTo
import java.net.URL

abstract class CMakeSetupTask : DefaultTask() {

    private val CMAKE_URL: String = "https://github.com/Kitware/CMake/releases/tag/"

    @get:Input
    abstract val platform: Property<String>

    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val installDir: DirectoryProperty

    @TaskAction
    fun downloadCmake() {
        logger.info("Initializing...")
        val url = URL("${CMAKE_URL}/${version.get()}.${parseZipExt(platform.get())}")
        logger.info("Downloading $url")
        val file = installDir.file(FilenameUtils.getName(url.toString()))
        url.openStream().use { ins ->
            file.get().asFile.writeBytes(ins.readBytes())
        }

        logger.info("Unzipping $file")
        unzipTo(installDir.get().asFile, file.get().asFile)

        setProperty("cmake.path", file.get().asFile.path)
    }

    private fun parseZipExt(platform: String): String = with(platform) {
        when {
            contains("windows") -> "zip"
            contains("mac") -> "tar.gz"
            contains("linux") -> "tar.gz"
            else -> "tar.gz"
        }
    }
}