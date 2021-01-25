package me.saro.test.kit.ee.kt

import me.saro.kit.ee.ftp.Ftp.Companion.ftp
import me.saro.kit.ee.ftp.Ftp.Companion.ftps
import me.saro.kit.ee.ftp.Ftp.Companion.sftp
import org.junit.jupiter.api.Test

/**
 * ftp test
 * @see  org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
class FtpTest {
    @Test
    fun test() {
        println("+++++++ FTP +++++++")
        ftp()

        println("+++++++ FTPS (explicit mode) +++++++")
        ftpsExplicitMode()

        println("+++++++ FTPS (implicit mode) +++++++")
        ftpsImplicitMode()

        println("+++++++ S-FTP +++++++")
        sftp()
    }

    fun ftp() = ftp("test.rebex.net", 21, "demo", "password")
        .beforeLogin { it.connectTimeout = 60000 }
        .open()
        .use { ftp ->
            println("ftp opened!!")
            println("pwd: ${ftp.pwd()}")
            ftp.listFiles().forEach { println("file: $it") }
            assert(true)
        }

    fun ftpsExplicitMode() = ftps(false, "test.rebex.net", 21, "demo", "password")
        .encoding("EUC-KR")
        .beforeLogin { it.connectTimeout = 60000 }
        .open()
        .use { ftp ->
            println("ftps (explicit mode) opened!!")
            println("pwd: ${ftp.pwd()}")
            ftp.listFiles().forEach { println("file: $it") }
            assert(true)
        }

    fun ftpsImplicitMode() =
        ftps(true, "test.rebex.net", 990, "demo", "password")
            .open()
            .use { ftp ->
                println("ftps (implicit mode) opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }

    fun sftp() =
        sftp("test.rebex.net", 22, "demo", "password")
            .beforeConnect { it.timeout = 120000; }
            .open()
            .use { ftp ->
                println("sftp opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }
}