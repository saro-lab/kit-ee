package me.saro.kit.ee.ftp

import com.jcraft.jsch.*
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.ChannelSftp.LsEntrySelector
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.function.Predicate

/**
 * SFTP
 * it is not FTPS
 * SFTP != FTPS
 */
class Sftp: Ftp {

    private var session: Session
    private var channelSftp: ChannelSftp

    internal constructor(session: Session, channelSftp: ChannelSftp) {
        this.session = session
        this.channelSftp = channelSftp
    }

    @Throws(IOException::class)
    override fun path(pathname: String): Boolean {
        try {
            channelSftp!!.cd(pathname)
        } catch (e: SftpException) {
            return false
        }
        return true
    }

    @Throws(IOException::class)
    override fun path(): String {
        return try {
            channelSftp!!.pwd()
        } catch (e: SftpException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    private fun list(filter: Predicate<LsEntry>): List<String> {
        return try {
            val list: MutableList<String> = ArrayList()
            channelSftp!!.ls(path()) { e: LsEntry ->
                if (filter.test(e)) {
                    list.add(e.filename)
                }
                LsEntrySelector.CONTINUE
            }
            list
        } catch (e: SftpException) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun listFiles(filter: Predicate<String>): List<String> {
        return list { e: LsEntry -> !e.attrs.isDir && filter.test(e.filename) }
    }

    @Throws(IOException::class)
    override fun listFiles(): List<String> {
        return list { e: LsEntry -> !e.attrs.isDir }
    }

    @Throws(IOException::class)
    override fun listDirectories(filter: Predicate<String>): List<String> {
        return list { e: LsEntry -> e.attrs.isDir && !e.filename.matches("[\\.]{1,2}") && filter.test(e.filename) }
    }

    @Throws(IOException::class)
    override fun listDirectories(): List<String> {
        return list { e: LsEntry -> e.attrs.isDir && !e.filename.matches("[\\.]{1,2}") }
    }

    @Throws(IOException::class)
    override fun hasFile(filename: String): Boolean {
        return try {
            Optional.ofNullable(channelSftp!!.lstat(path() + "/" + filename))
                .filter { e: SftpATTRS -> !e.isDir }.isPresent
        } catch (e: SftpException) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false
            }
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun hasDirectory(directoryName: String): Boolean {
        return try {
            Optional.ofNullable(channelSftp!!.lstat(path() + "/" + directoryName))
                .filter { e: SftpATTRS -> e.isDir }.isPresent
        } catch (e: SftpException) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false
            }
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun delete(filename: String): Boolean {
        return try {
            if (hasFile(filename)) {
                channelSftp!!.rm(filename)
                return true
            } else if (hasDirectory(filename)) {
                channelSftp!!.rmdir(filename)
                return true
            }
            false
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun send(saveFilename: String, localFile: File): Boolean {
        try {
            FileInputStream(localFile).use { input -> channelSftp!!.put(input, saveFilename) }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return true
    }

    @Throws(IOException::class)
    override fun recv(remoteFilename: String, localFile: File): Boolean {
        if (!hasFile(remoteFilename)) {
            return false
        }
        if (localFile.exists()) {
            localFile.delete()
        }
        try {
            FileOutputStream(localFile).use { fos ->
                channelSftp!![remoteFilename, fos]
                return true
            }
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun mkdir(createDirectoryName: String): Boolean {
        try {
            channelSftp!!.mkdir(createDirectoryName)
        } catch (e: SftpException) {
            return false
        }
        return true
    }

    override fun close() {
        try {
            channelSftp!!.disconnect()
        } catch (e: Exception) {
        }
        try {
            session!!.disconnect()
        } catch (e: Exception) {
        }
    }

    companion object class SFTPBuilder {
        private val open: (JSch) -> Session
        private val cmd = mutableMapOf<String, (Session) -> Unit>()

        internal constructor(host: String, port: Int, username: String = "anonymous", password: ByteArray = ByteArray(0)) {
            open = { it.getSession(username, host, port) }
            cmd["open"] = { e -> e.setPassword(password) }
        }

        fun custom(exec: (Session) -> Unit) =
            this.apply { cmd["custom"] = exec }

        fun strictHostKeyChecking(value: String) =
            this.apply { cmd["strictHostKeyChecking"] = { it.setConfig("StrictHostKeyChecking", "no") } }

        init {
            strictHostKeyChecking("no")
        }

        fun open(): Ftp {
            val session = JSch().run { open(this) }
            cmd.values.forEach { it(session) }
            return Sftp(session, (session.openChannel("sftp") as ChannelSftp).apply { this.connect() })
        }
    }
}