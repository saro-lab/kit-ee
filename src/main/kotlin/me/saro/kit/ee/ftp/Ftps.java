package me.saro.kit.ee.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FTP, FTPS
 */
public class Ftps implements Ftp {

    final FTPClient ftp;

    public Ftps(FtpsOpener opener) throws IOException {
        FTPClient ftp = null;
        try {
            ftp = opener.open();
        } catch (IOException e) {
            try { if (ftp != null) { ftp.disconnect(); } } catch (Exception e1) { }
            throw e;
        }
        this.ftp = ftp;
    }

    public Ftps(FtpOpener opener) throws IOException {
        FTPClient ftp = null;
        try {
            ftp = opener.open();
        } catch (IOException e) {
            try { if (ftp != null) { ftp.disconnect(); } } catch (Exception e1) { }
            throw e;
        }
        this.ftp = ftp;
    }
    
    /**
     * BINARY FILE MODE<br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    public void enterBinaryFileMode() throws IOException {
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    /**
     * ASCII FILE MODE<br>
     * default : BINARY FILE MODE
     * @throws IOException
     */
    public void enterAsciiFileMode() throws IOException {
        ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
    }
    
    /**
     * on passive mode<br>
     * default value : passive mode
     */
    public void enterLocalPassiveMode() {
        ftp.enterLocalPassiveMode();
    }
    
    /**
     * on active mode<br>
     * default value : passive mode
     */
    public void enterLocalActiveMode() {
        ftp.enterLocalActiveMode();
    }
    
    /**
     * user Extended Passive Mode with IPv4<br>
     * default false
     * @param selected
     */
    public void setUseEPSVwithIPv4(boolean selected) {
        ftp.setUseEPSVwithIPv4(selected);
    }
    
    @Override
    public boolean path(String pathname) throws IOException {
        return ftp.changeWorkingDirectory(pathname);
    }

    @Override
    public String path() throws IOException {
        return ftp.printWorkingDirectory();
    }

    @Override
    public boolean hasFile(String filename) throws IOException {
        FTPFile ff = ftp.mlistFile(filename);
        if (ff != null) {
            return ff.isFile();
        } else {
            return listFiles().parallelStream().anyMatch(e -> filename.equals(e));
        }
    }

    @Override
    public boolean hasDirectory(String directoryName) throws IOException {
        FTPFile ff = ftp.mlistFile(path() + "/" + directoryName);
        if (ff != null) {
            return ff.isDirectory();
        } else {
            return listDirectories().parallelStream().anyMatch(e -> directoryName.equals(e));
        }
    }

    @Override
    public List<String> listFiles(Predicate<String> filter) throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isFile()).map(e -> e.getName()).filter(filter).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listFiles() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isFile()).map(e -> e.getName()).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listDirectories(Predicate<String> filter) throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isDirectory()).map(e -> e.getName()).filter(filter).collect(Collectors.toList());
    }
    
    @Override
    public List<String> listDirectories() throws IOException {
        return Stream.of(ftp.listFiles()).filter(e -> e.isDirectory()).map(e -> e.getName()).collect(Collectors.toList());
    }

    @Override
    public boolean delete(String filename) throws IOException {
        if (hasFile(filename)) {
            return ftp.deleteFile(filename);
        } else if (hasDirectory(filename)) {
            return ftp.removeDirectory(filename);
        }
        return false;
    }

    @Override
    public boolean send(String saveFilename, File localFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            return ftp.storeFile(saveFilename, fis);
        }
    }

    @Override
    public boolean recv(String remoteFilename, File localFile) throws IOException {
        if (hasFile(remoteFilename)) {
            if (localFile.exists()) {
                localFile.delete();
            }
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                return ftp.retrieveFile(remoteFilename, fos);
            }   
        } else {
            return false;
        }
    }
    
    @Override
    public boolean mkdir(String createDirectoryName) throws IOException {
        ftp.mkd(createDirectoryName);
        return true;
    }

    @Override
    public void close() {
        try {
            ftp.disconnect();
        } catch (IOException e) {
        }
    }

    public FTPClient getFTPClient() {
        return ftp;
    }

    public static interface FtpOpener {
        FTPClient open() throws IOException;
    }

    public static interface FtpsOpener {
        FTPSClient open() throws IOException;
    }
}
