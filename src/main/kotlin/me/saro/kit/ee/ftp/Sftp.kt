package me.saro.kit.ee.ftp

import com.jcraft.jsch.*
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.ChannelSftp.LsEntrySelector
import me.saro.kit.legacy.ThrowableConsumer
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
class Sftp : Ftp {

    private val builder: Builder
    private val session: Session
    private val channelSftp: ChannelSftp

    @Throws(IOException::class)
    internal constructor(builder: Builder) {
        try {
            this.builder = builder
            val jSch = JSch()
            builder.beforeSession.forEach { it.accept(jSch) }
            this.session = jSch.getSession(builder.username, builder.host, builder.port)
            builder.password?.run { session.setPassword(this) }
            builder.beforeConnect.forEach { it.accept(session) }
            session.connect()
            this.channelSftp = (session.openChannel("sftp") as ChannelSftp).apply { this.connect() }
        } catch (e: Exception) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun path(pathname: String): Boolean = tryReturn { channelSftp.cd(pathname) }

    @Throws(IOException::class)
    override fun path(): String = tryOut { channelSftp.pwd() }

    @Throws(IOException::class)
    private fun list(filter: Predicate<LsEntry>): List<String> = tryOut {
        ArrayList<String>().apply {
            channelSftp.ls(path()) { ls: LsEntry ->
                if (filter.test(ls)) { this.add(ls.filename) }
                LsEntrySelector.CONTINUE
            }
        }
    }

    @Throws(IOException::class)
    override fun listFiles(): List<String> = list { e: LsEntry -> !e.attrs.isDir }

    @Throws(IOException::class)
    override fun listFiles(filter: Predicate<String>): List<String> =
        list { e: LsEntry -> !e.attrs.isDir && filter.test(e.filename) }

    @Throws(IOException::class)
    override fun listDirectories(filter: Predicate<String>): List<String> =
        list { e: LsEntry -> e.attrs.isDir && !e.filename.matches("[\\.]{1,2}".toRegex()) && filter.test(e.filename) }

    @Throws(IOException::class)
    override fun listDirectories(): List<String> =
        list { e: LsEntry -> e.attrs.isDir && !e.filename.matches("[\\.]{1,2}".toRegex()) }

    @Throws(IOException::class)
    override fun hasFile(filename: String): Boolean {
        return try {
            Optional.ofNullable(channelSftp.lstat(path() + "/" + filename)).filter { e: SftpATTRS -> !e.isDir }.isPresent
        } catch (e: SftpException) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) { return false }
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun hasDirectory(directoryName: String): Boolean {
        return try {
            Optional.ofNullable(channelSftp.lstat(path() + "/" + directoryName)).filter { e: SftpATTRS -> e.isDir }.isPresent
        } catch (e: SftpException) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) { return false }
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun delete(filename: String): Boolean = tryOut {
        when {
            hasFile(filename) -> { channelSftp.rm(filename); true }
            hasDirectory(filename) -> { channelSftp.rmdir(filename); true }
            else -> false
        }
    }

    @Throws(IOException::class)
    override fun send(saveFilename: String, localFile: File): Boolean = tryOut {
        FileInputStream(localFile).use { input -> channelSftp.put(input, saveFilename) }; true
    }

    @Throws(IOException::class)
    override fun recv(remoteFilename: String, localFile: File): Boolean = tryOut {
        if (!hasFile(remoteFilename)) { return@tryOut false }
        if (localFile.exists()) { localFile.delete() }
        FileOutputStream(localFile).use { fos -> channelSftp[remoteFilename, fos] }
        return@tryOut true
    }

    @Throws(IOException::class)
    override fun mkdir(createDirectoryName: String): Boolean = tryReturn { channelSftp.mkdir(createDirectoryName) }

    override fun close() {
        try { channelSftp.disconnect() } catch (e: Exception) { }
        try { session.disconnect() } catch (e: Exception) { }
    }

    private fun tryReturn(fn: () -> Unit): Boolean = try { fn(); true } catch (e: SftpException) { false }

    @Throws(IOException::class)
    private fun <T> tryOut(fn: () -> T): T = try { fn() } catch (e: Exception) { throw IOException(e) }

    companion object class Builder internal constructor(
        internal val host: String,
        internal val port: Int,
        internal var username: String = "",
        internal var password: ByteArray? = null
    ) {
        internal val beforeSession = ArrayList<ThrowableConsumer<JSch>>()
        internal val beforeConnect = ArrayList<ThrowableConsumer<Session>>()

        // default setting
        init {
            strictHostKeyChecking("no")
        }

        fun strictHostKeyChecking(value: String) =
            beforeConnect { it.setConfig("StrictHostKeyChecking", value) }

        fun beforeSession(exec: ThrowableConsumer<JSch>) = this.apply { beforeSession.add(exec) }
        fun beforeConnect(exec: ThrowableConsumer<Session>) = this.apply { beforeConnect.add(exec) }

        @Throws(IOException::class)
        fun userAnonymous(): Builder = this.apply { this.username = "anonymous" }

        fun user(username: String, password: String): Builder = user(username, password.toByteArray())

        fun user(username: String): Builder = this.apply { this.username = username }

        fun user(username: String, password: ByteArray): Builder = this.apply { this.username = username; this.password = password }

        fun userPublicKey(username: String, privateKey: String): Builder =
            this.apply {
                beforeSession { it.addIdentity(privateKey) }
                beforeConnect { it.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password") }
            }

        @Throws(IOException::class)
        fun open(): Ftp = Sftp(this)
    }
}
