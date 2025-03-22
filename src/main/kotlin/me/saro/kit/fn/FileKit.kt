package me.saro.kit.fn

import java.io.File
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files.walk
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class FileKit {
    companion object {
        @JvmStatic
        fun openInJar(clazz: Class<*>, directoryPath: String, action: (Path) -> Unit) {
            val jarPath = clazz.protectionDomain.codeSource.location.toURI().path
            if (jarPath.endsWith(".jar")) { // in jar
                FileSystems.newFileSystem(URI.create("jar:file:$jarPath"), mutableMapOf<String, Any>()).use {
                    walk(it.getPath(directoryPath)).forEach(action)
                }
            } else { // ide mode
                File(clazz.getResource(directoryPath)!!.toURI()).listFiles()?.forEach { file -> action(file.toPath()) }
            }
        }
        
        @JvmStatic
        fun unzip(zipFile: File, destDir: File) =
            unzip(zipFile) { File(destDir, it.name) }

        @JvmStatic
        fun unzip(zipFile: File, zipRootDepth: Int, destDir: File) =
            unzip(zipFile) {
                val paths = it.name.split("/")
                if (paths.size > zipRootDepth) {
                    File(destDir, paths.drop(zipRootDepth).joinToString("/"))
                } else null
            }

        @JvmStatic
        fun unzip(zipFile: File, eachSavePath: (ZipEntry) -> File?) =
            ZipFile(zipFile).use { zip -> zip.entries().asSequence().forEach { entry ->
                eachSavePath(entry)?.also { file ->
                    if (entry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile.mkdirs()
                        file.outputStream().use { output -> zip.getInputStream(entry).copyTo(output) }
                    }
                }
            }}
    }
}
