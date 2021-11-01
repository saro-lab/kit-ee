package me.saro.kit.ee.ftp

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPSClient
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.util.function.Predicate

/**
 * ftp
 * <br></br>
 * simple ftp class
 * @author        PARK Yong Seo
 * @see  FTPClient
 * com.jcraft.jsch.JSch
 */
interface Ftp : Closeable {

    companion object {
        @JvmStatic
        fun ftp(host: String, port: Int, username: String, password: String, charset: String) =
            Ftps.Builder(false, FTPClient(), host, port).user(username, password).encoding(charset)

        @JvmStatic
        fun ftp(host: String, port: Int, username: String, password: String) =
            Ftps.Builder(false, FTPClient(), host, port).user(username, password)

        @JvmStatic
        fun ftp(host: String, port: Int) =
            Ftps.Builder(false, FTPClient(), host, port)

        @JvmStatic
        fun ftps(isImplicit: Boolean, host: String, port: Int, username: String, password: String, charset: String) =
            Ftps.Builder(true, FTPSClient(isImplicit), host, port).user(username, password).encoding(charset)

        @JvmStatic
        fun ftps(isImplicit: Boolean, host: String, port: Int, username: String, password: String) =
            Ftps.Builder(true, FTPSClient(isImplicit), host, port).user(username, password)

        @JvmStatic
        fun ftps(isImplicit: Boolean, host: String, port: Int) =
            Ftps.Builder(true, FTPSClient(isImplicit), host, port)

        @JvmStatic
        fun sftp(host: String, port: Int, username: String, password: String) =
            Sftp.Builder(host, port).user(username, password)

        @JvmStatic
        fun sftp(host: String, port: Int) =
            Sftp.Builder(host, port)
    }

    /**
     * move path<br></br>
     * same method path(), cd()
     * @param pathname
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun path(pathname: String): Boolean

    /**
     * get now path<br></br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun path(): String

    /**
     * get file list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listFiles(filter: Predicate<String>): List<String>

    /**
     * get file list in now path
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listFiles(): List<String>

    /**
     * get directory list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listDirectories(filter: Predicate<String>): List<String>

    /**
     * get directory list in now path
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listDirectories(): List<String>

    /**
     * has file in path
     * @param filename
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun hasFile(filename: String): Boolean

    /**
     * has directory in path
     * @param directoryName
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun hasDirectory(directoryName: String): Boolean

    /**
     * remove file
     * @param filename
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun delete(filename: String): Boolean

    /**
     * send file
     * @param saveFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun send(saveFilename: String, localFile: File): Boolean

    /**
     * make new directory
     * @param createDirectoryName
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun mkdir(createDirectoryName: String): Boolean

    /**
     * recv file
     * @param remoteFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun recv(remoteFilename: String, localFile: File): Boolean

    /**
     * close
     */
    override fun close()

    /**
     * change directory<br></br>
     * same method path(), cd()
     * @param pathname
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun cd(pathname: String): Boolean = path(pathname)

    /**
     * print working directory<br></br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun pwd(): String = path()

    /**
     * send file
     * @param localFile
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun send(localFile: File): Boolean = send(localFile.name, localFile)



    /**
     * recv file list
     * @param remoteFilenameList
     * @param localDirectory
     * @return
     */
    @Throws(IOException::class)
    fun recv(remoteFilenameList: List<String>, localDirectory: File) {
        if (!localDirectory.isDirectory) {
            throw IOException("[" + localDirectory.absolutePath + "] is not Directory")
        }
        for (file in remoteFilenameList) {
            if (!recv(file, File(localDirectory, file))) {
                throw IOException("fail recv file [" + path() + "/" + file + "]")
            }
        }
    }
}