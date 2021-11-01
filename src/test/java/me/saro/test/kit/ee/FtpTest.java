package me.saro.test.kit.ee;

import me.saro.kit.ee.ftp.Ftp;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * ftp test
 * @see
 * org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
public class FtpTest {

    @Test
    public void test() throws Exception {
//        System.out.println("+++++++ FTP +++++++");
//        ftp();
//
//        System.out.println("+++++++ FTPS (explicit mode) +++++++");
//        ftpsExplicitMode();
//
//        System.out.println("+++++++ FTPS (implicit mode) +++++++");
//        ftpsImplicitMode();
//
//        System.out.println("+++++++ S-FTP +++++++");
//        sftp();
    }

    public void ftp() throws IOException {
        try (Ftp ftp = Ftp.ftp("test.rebex.net", 21, "demo", "password").open()) {
            System.out.println("ftp opened!!");
            System.out.println("pwd: " + ftp.pwd());
            ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
            assert(true);
        }
    }

    public void ftpsExplicitMode() throws IOException {
        try (Ftp ftp = Ftp.ftps(false, "test.rebex.net", 21, "demo", "password").open()) {
            System.out.println("ftps (explicit mode) opened!!");
            System.out.println("pwd: " + ftp.pwd());
            ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
            assert(true);
        }
    }

    public void ftpsImplicitMode() throws IOException {
        try (
                Ftp ftp = Ftp
                        .ftps(true, "test.rebex.net", 990, "demo", "password")
                        .encoding("UTF-8")
                        .open()
        ) {
            System.out.println("ftps (implicit mode) opened!!");
            System.out.println("pwd: " + ftp.pwd());
            ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
            assert(true);
        }
    }

    public void sftp() throws IOException {
        try (Ftp ftp = Ftp.sftp("test.rebex.net", 22, "demo", "password").open()) {
            System.out.println("sftp opened!!");
            System.out.println("pwd: " + ftp.pwd());
            ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
            assert(true);
        }
    }

    public void sftpPublicKey() throws IOException {
        try (Ftp ftp = Ftp.sftp("public-key.test.com", 22).userPublicKey("demo", "/user/.ssh/id_rsa").open()) {
            System.out.println("sftp opened!!");
            System.out.println("pwd: " + ftp.pwd());
            ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
            assert(true);
        }
    }
}

