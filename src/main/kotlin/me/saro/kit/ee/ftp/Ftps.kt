package me.saro.kit.ee.ftp

import me.saro.kit.legacy.ThrowableConsumer
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPSClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * FTP, FTPS
 */
class Ftps : Ftp {
    private val ftp: FTPClient
    private val builder: Builder
    private val secure: Boolean

    @Throws(IOException::class)
    internal constructor(builder: Builder) {
        try {
            // define
            this.builder = builder
            this.ftp = builder.ftpClient
            this.secure = builder.secure
            // before connection
            builder.beforeConnection.forEach { it.accept(ftp) }
            // connecting
            ftp.connect(builder.host, builder.port)
            // after connected
            if (secure) {
                val ftps = ftp as FTPSClient
                ftps.execPBSZ(0)
                ftps.execPROT("P")
            }
            ftp.enterLocalPassiveMode()
            ftp.isUseEPSVwithIPv4 = false
            ftp.controlKeepAliveReplyTimeout = 60000
            builder.beforeLogin.forEach { it.accept(ftp) }
            if (!ftp.login(builder.username, builder.password)) {
                throw IOException("login fail")
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE)
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    /**
     * get raw ftp client
     */
    val ftpClient: FTPClient get() = ftp

    /**
     * BINARY FILE MODE<br></br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    @Throws(IOException::class)
    fun enterBinaryFileMode() = ftp.setFileType(FTPClient.BINARY_FILE_TYPE)

    /**
     * ASCII FILE MODE<br></br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    @Throws(IOException::class)
    fun enterAsciiFileMode() = ftp.setFileType(FTPClient.ASCII_FILE_TYPE)

    /**
     * on passive mode<br></br>
     * default value : passive mode
     */
    fun enterLocalPassiveMode() = ftp.enterLocalPassiveMode()

    /**
     * on active mode<br></br>
     * default value : passive mode
     */
    fun enterLocalActiveMode() = ftp.enterLocalActiveMode()

    /**
     * user Extended Passive Mode with IPv4<br></br>
     * default false
     * @param selected
     */
    fun setUseEPSVwithIPv4(selected: Boolean) { ftp.isUseEPSVwithIPv4 = selected }

    @Throws(IOException::class)
    override fun path(pathname: String): Boolean = ftp.changeWorkingDirectory(pathname)

    @Throws(IOException::class)
    override fun path(): String = ftp.printWorkingDirectory()

    @Throws(IOException::class)
    override fun hasFile(filename: String): Boolean =
        ftp.mlistFile(filename)?.isFile ?: listFiles().parallelStream().anyMatch { e: String -> filename == e }

    @Throws(IOException::class)
    override fun hasDirectory(directoryName: String): Boolean =
        ftp.mlistFile(path() + "/" + directoryName)?.isDirectory
            ?: listDirectories().parallelStream().anyMatch { e: String -> directoryName == e }

    @Throws(IOException::class)
    override fun listFiles(filter: Predicate<String>): List<String> =
        Stream.of(*ftp.listFiles()).filter { e: FTPFile -> e.isFile }
            .map { e: FTPFile -> e.name }.filter(filter).collect(Collectors.toList())

    @Throws(IOException::class)
    override fun listFiles(): List<String> =
        Stream.of(*ftp.listFiles()).filter { e: FTPFile -> e.isFile }
            .map { e: FTPFile -> e.name }.collect(Collectors.toList())

    @Throws(IOException::class)
    override fun listDirectories(filter: Predicate<String>): List<String> =
        Stream.of(*ftp.listFiles()).filter { e: FTPFile -> e.isDirectory }
            .map { e: FTPFile -> e.name }.filter(filter).collect(Collectors.toList())

    @Throws(IOException::class)
    override fun listDirectories(): List<String> =
        Stream.of(*ftp.listFiles()).filter { e: FTPFile -> e.isDirectory }
            .map { e: FTPFile -> e.name }.collect(Collectors.toList())

    @Throws(IOException::class)
    override fun delete(filename: String): Boolean =
        when {
            hasFile(filename) -> ftp.deleteFile(filename)
            hasDirectory(filename) -> ftp.removeDirectory(filename)
            else -> false
        }

    @Throws(IOException::class)
    override fun send(saveFilename: String, localFile: File): Boolean =
        FileInputStream(localFile).use { fis -> return ftp.storeFile(saveFilename, fis) }

    @Throws(IOException::class)
    override fun recv(remoteFilename: String, localFile: File): Boolean =
        if (hasFile(remoteFilename)) {
            if (localFile.exists()) { localFile.delete() }
            FileOutputStream(localFile).use { fos -> return ftp.retrieveFile(remoteFilename, fos) }
        } else false

    @Throws(IOException::class)
    override fun mkdir(createDirectoryName: String): Boolean { ftp.mkd(createDirectoryName); return true }

    override fun close(): Unit = try { ftp.disconnect() } catch (e: IOException) { }

    companion object class Builder internal constructor(
        internal val secure: Boolean,
        internal val ftpClient: FTPClient,
        internal val host: String,
        internal val port: Int,
        internal var username: String = "",
        internal var password: String = "",
    ) {
        internal val beforeConnection = ArrayList<ThrowableConsumer<FTPClient>>()
        internal val beforeLogin = ArrayList<ThrowableConsumer<FTPClient>>()

        // default setting
        init {
            strictReplyParsing(false)
        }

        fun strictReplyParsing(value: Boolean) = beforeConnection { it.isStrictReplyParsing = value }

        fun encoding(charset: String) = beforeConnection { it.controlEncoding = charset }

        /** this function will execute before connection */
        fun beforeConnection(fn: ThrowableConsumer<FTPClient>) = this.apply { beforeConnection.add(fn) }

        /** this function will execute before login */
        fun beforeLogin(fn: ThrowableConsumer<FTPClient>) = this.apply { beforeLogin.add(fn) }

        @Throws(IOException::class)
        fun userAnonymous(): Builder = this.apply { this.username = "anonymous" }

        fun user(username: String, password: String): Builder = this.apply { this.username = username; this.password = password }

        @Throws(IOException::class)
        fun open(): Ftp = Ftps(this)
    }
}