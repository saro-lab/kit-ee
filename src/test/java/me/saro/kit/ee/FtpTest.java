package me.saro.kit.ee;

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
        // ftp
        ftp();
        // ftps (explicit mode)
        ftpsExplicitMode();
        // ftps (implicit mode)
        ftpsImplicitMode();
        // sftp
        sftp();
    }

    public void ftp() throws IOException {
        try (Ftp ftp = Ftp.ftp("test.rebex.net", 21, "demo", "password")) {
            System.out.println("ftp opened!!");
            assert(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ftp error!!");
        }
    }

    public void ftpsExplicitMode() throws IOException {
        try (Ftp ftp = Ftp.ftps(false, "test.rebex.net", 21, "demo", "password")) {
            System.out.println("ftps (explicit mode) opened!!");
            assert(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ftps (explicit mode) error!!");
        }
    }

    public void ftpsImplicitMode() throws IOException {
        try (Ftp ftp = Ftp.ftps(true, "test.rebex.net", 990, "demo", "password")) {
            System.out.println("ftps (implicit mode) opened!!");
            assert(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ftps (implicit mode) error!!");
        }
    }

    public void sftp() throws IOException {
        try (Ftp ftp = Ftp.sftp("test.rebex.net", 22, "demo", "password")) {
            System.out.println("sftp opened!!");
            assert(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("sftp error!!");
        }
    }
}

