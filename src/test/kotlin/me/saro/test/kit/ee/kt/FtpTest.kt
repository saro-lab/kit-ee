package me.saro.test.kit.ee.kt

import me.saro.kit.ee.ftp.Ftp
import org.junit.jupiter.api.Test

/**
 * ftp test
 * @see  org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
class FtpTest {
    @Test
    fun test() {
//        println("+++++++ FTP +++++++")
//        ftp()
//
//        println("+++++++ FTPS (explicit mode) +++++++")
//        ftpsExplicitMode()
//
//        println("+++++++ FTPS (implicit mode) +++++++")
//        ftpsImplicitMode()
//
//        println("+++++++ S-FTP +++++++")
//        sftp()
    }

    fun ftp() =
        Ftp.ftp("test.rebex.net", 21, "demo", "password")
            .beforeLogin { it.connectTimeout = 60000 }
            .open()
            .use { ftp ->
                println("ftp opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }

    fun ftpsExplicitMode() =
        Ftp.ftps(false, "test.rebex.net", 21, "demo", "password")
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
        Ftp.ftps(true, "test.rebex.net", 990, "demo", "password")
            .open()
            .use { ftp ->
                println("ftps (implicit mode) opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }

    fun sftp() =
        Ftp.sftp("test.rebex.net", 22, "demo", "password")
            .beforeConnect { it.timeout = 120000; }
            .open()
            .use { ftp ->
                println("sftp opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }

    // does not found public-key test-server
    fun sftpPublicKey() =
        Ftp.sftp("public-key.test.com", 22)
            .userPublicKey("demo", "/user/.ssh/id_rsa")
            .beforeConnect { it.timeout = 120000; }
            .open()
            .use { ftp ->
                println("sftp opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }
}