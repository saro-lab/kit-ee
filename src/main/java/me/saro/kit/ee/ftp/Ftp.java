package me.saro.kit.ee.ftp;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * ftp
 * <br>
 * simple ftp class
 * @author		PARK Yong Seo
 * @see
 * org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
public interface Ftp extends Closeable {

    static Ftp ftp(Ftps.FtpOpener opener) throws IOException {
        return new Ftps(opener);
    }

    static Ftp ftp(String host, int port, String user, String pass) throws IOException {
        return ftp(() -> {
            FTPClient ftp = new FTPClient();
            ftp.setStrictReplyParsing(false);
            ftp.connect(host, port);
            ftp.enterLocalPassiveMode();
            ftp.setUseEPSVwithIPv4(false);
            if (!ftp.login(user, pass)) {
                throw new IOException("login fail");
            }
            ftp.setControlKeepAliveReplyTimeout(60000);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            return ftp;
        });
    }

    static Ftp ftp(String host, int port) throws IOException {
        return ftp(host, port, "anonymous", "");
    }

    static Ftp ftps(Ftps.FtpsOpener opener) throws IOException {
        return new Ftps(opener);
    }

    static Ftp ftps(boolean isImplicit, String host, int port, String user, String pass) throws IOException {
        return ftps(() -> {
            FTPSClient ftps = new FTPSClient(isImplicit);
            ftps.setStrictReplyParsing(false);
            ftps.connect(host, port);
            ftps.execPBSZ(0);
            ftps.execPROT("P");
            ftps.enterLocalPassiveMode();
            ftps.setUseEPSVwithIPv4(false);
            if (!ftps.login(user, pass)) {
                throw new IOException("login fail");
            }
            ftps.setControlKeepAliveReplyTimeout(60000);
            ftps.setFileType(FTPClient.BINARY_FILE_TYPE);
            return ftps;
        });
    }

    static Ftp ftps(boolean isImplicit, String host, int port) throws IOException {
        return ftps(isImplicit, host, port, "anonymous", "");
    }

    static Ftp sftp(String host, int port, String user, Sftp.SftpOpener opener) throws IOException {
        return new Sftp(host, port, user, opener);
    }

    static Ftp sftp(String host, int port, String user, String pass) throws IOException {
        return sftp(host, port, user, session -> {
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            return sftp;
        });
    }

    static Ftp sftp(String host, int port) throws IOException {
        return sftp(host, port, "anonymous", "");
    }
    
    /**
     * change directory<br>
     * same method path(), cd()
     * @param pathname
     * @return
     * @throws IOException 
     */
    default boolean cd(String pathname) throws IOException {
        return path(pathname);
    }
    
    /**
     * move path<br>
     * same method path(), cd()
     * @param pathname
     * @return
     * @throws IOException
     */
    boolean path(String pathname) throws IOException;

    /**
     * get now path<br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    String path() throws IOException;
    
    /**
     * print working directory<br>
     * same method path(), pwd()
     * @return
     * @throws IOException
     */
    default String pwd() throws IOException {
        return path();
    }
    
    /**
     * get file list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    List<String> listFiles(Predicate<String> filter) throws IOException;
    
    /**
     * get file list in now path
     * @return
     * @throws IOException
     */
    List<String> listFiles() throws IOException;
    
    /**
     * get directory list in now path
     * @param filter
     * file name filter
     * @return
     * @throws IOException
     */
    List<String> listDirectories(Predicate<String> filter) throws IOException;
    
    /**
     * get directory list in now path
     * @return
     * @throws IOException
     */
    List<String> listDirectories() throws IOException;
    
    /**
     * has file in path
     * @param filename
     * @return
     * @throws IOException
     */
    boolean hasFile(String filename) throws IOException;
    
    /**
     * has directory in path
     * @param directoryName
     * @return
     * @throws IOException
     */
    boolean hasDirectory(String directoryName) throws IOException;
    
    /**
     * remove file
     * @param filename
     * @return
     * @throws IOException
     */
    boolean delete(String filename) throws IOException;
    
    /**
     * send file
     * @param saveFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    boolean send(String saveFilename, File localFile) throws IOException;
    
    /**
     * send file
     * @param localFile
     * @return
     * @throws IOException
     */
    default boolean send(File localFile) throws IOException {
        return send(localFile.getName(), localFile);
    }

    /**
     * recv file
     * @param remoteFilename
     * @param localFile
     * @return
     * @throws IOException
     */
    boolean recv(String remoteFilename, File localFile) throws IOException;
    
    /**
     * recv file list
     * @param remoteFilenameList
     * @param localDirectory
     * @return
     */
    default void recv(List<String> remoteFilenameList, File localDirectory) throws IOException {
        if (!localDirectory.isDirectory()) {
            throw new IOException("["+localDirectory.getAbsolutePath()+"] is not Directory");
        }
        for (String file : remoteFilenameList) {
            if (!recv(file, new File(localDirectory, file))) {
                throw new IOException("fail recv file ["+path() + "/" + file + "]");
            }
        }
    }
    
    /**
     * make new directory
     * @param createDirectoryName
     * @return
     * @throws IOException
     */
    boolean mkdir(String createDirectoryName) throws IOException;
    
    /**
     * close
     */
    void close();
}
