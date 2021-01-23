package me.saro.kit.ee.ftp

import java.io.Closeable
import kotlin.Throws
import java.io.IOException
import java.io.File
import org.apache.commons.net.ftp.FTPClient
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
//        @Throws(IOException::class)
//        fun ftp(opener: FtpOpener?): FTP? {
//            return Ftps(opener)
//        }
//
//        @JvmStatic
//        @JvmOverloads
//        @Throws(IOException::class)
//        fun ftp(host: String, port: Int, user: String = "anonymous", pass: String = ""): FTP? {
//            return ftp {
//                val ftp = FTPClient()
//                ftp.isStrictReplyParsing = false
//                ftp.connect(host, port)
//                ftp.enterLocalPassiveMode()
//                ftp.isUseEPSVwithIPv4 = false
//                if (!ftp.login(user, pass)) {
//                    throw IOException("login fail")
//                }
//                ftp.controlKeepAliveReplyTimeout = 60000
//                ftp.setFileType(FTPClient.BINARY_FILE_TYPE)
//                ftp
//            }
//        }
//
//        @Throws(IOException::class)
//        fun ftps(opener: FtpsOpener?): FTP? {
//            return Ftps(opener)
//        }
//
//        @JvmStatic
//        @JvmOverloads
//        @Throws(IOException::class)
//        fun ftps(isImplicit: Boolean, host: String, port: Int, user: String = "anonymous", pass: String = ""): FTP? {
//            return ftps {
//                val ftps = FTPSClient(isImplicit)
//                ftps.isStrictReplyParsing = false
//                ftps.connect(host, port)
//                ftps.execPBSZ(0)
//                ftps.execPROT("P")
//                ftps.enterLocalPassiveMode()
//                ftps.isUseEPSVwithIPv4 = false
//                if (!ftps.login(user, pass)) {
//                    throw IOException("login fail")
//                }
//                ftps.controlKeepAliveReplyTimeout = 60000
//                ftps.setFileType(FTPClient.BINARY_FILE_TYPE)
//                ftps
//            }
//        }
//
//        @Throws(IOException::class)
//        fun sftp(host: String, port: Int, user: String, opener: SFTP.SftpOpener?): FTP? {
//            return SFTP(host, port, user, opener)
//        }
//
//        @JvmStatic
//        @JvmOverloads
//        @Throws(IOException::class)
//        fun sftp(host: String, port: Int, user: String = "anonymous", pass: String = ""): FTP? {
//            return sftp(host, port, user, SFTP.SftpOpener { session: Session ->
//                session.setPassword(pass)
//                session.setConfig("StrictHostKeyChecking", "no")
//                session.connect()
//                val sftp = session.openChannel("sftp") as ChannelSftp
//                sftp.connect()
//                sftp
//            })
//        }

        fun sftpAnonymous(host: String, port: Int) =
            Sftp.SFTPBuilder(host, port)

        fun sftp(host: String, port: Int, username: String, password: String) =
            Sftp.SFTPBuilder(host, port, username, password.toByteArray())

        fun sftp(host: String, port: Int, username: String, password: ByteArray) =
            Sftp.SFTPBuilder(host, port, username, password)
    }

    /**
     * change directory<br></br>
     * same method path(), cd()
     * @param pathname
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun cd(pathname: String): Boolean {
        return path(pathname)
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
     * print working directory<br></br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun pwd(): String {
        return path()
    }

    /**
     * get file list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listFiles(filter: Predicate<String>?): List<String>?

    /**
     * get file list in now path
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listFiles(): List<String>?

    /**
     * get directory list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listDirectories(filter: Predicate<String>?): List<String>?

    /**
     * get directory list in now path
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun listDirectories(): List<String>?

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
    fun send(saveFilename: String, localFile: File?): Boolean

    /**
     * send file
     * @param localFile
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun send(localFile: File): Boolean {
        return send(localFile.name, localFile)
    }

    /**
     * recv file
     * @param remoteFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun recv(remoteFilename: String, localFile: File?): Boolean

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

    /**
     * make new directory
     * @param createDirectoryName
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun mkdir(createDirectoryName: String): Boolean

    /**
     * close
     */
    override fun close()
}