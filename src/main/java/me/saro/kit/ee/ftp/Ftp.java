package me.saro.kit.ee.ftp;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.function.Predicate;

/**
 * ftp
 * <br>
 * simple ftp class
 * @author		PARK Yong Seo
 * @since		0.2
 * @see
 * org.apache.commons.net.ftp.FTPClient
 * com.jcraft.jsch.JSch
 */
public interface Ftp extends Closeable {
    
    /**
     * open ftp
     * @param host
     * ip or domain
     * @param port
     * port (ftp basic port 21)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    static Ftp ftp(InetAddress host, int port, String user, String pass) throws IOException {
        return new Ftps(host, port, user, pass, false);
    }
    
    /**
     * open ftp
     * @param host
     * ip or domain
     * @param port
     * port (ftp basic port 21)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    static Ftp ftp(String host, int port, String user, String pass) throws IOException {
        return new Ftps(InetAddress.getByName(host), port, user, pass, false);
    }
    
    /**
     * open ftps
     * @param host
     * ip or domain
     * @param port
     * port (ftps basic port 990)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    static Ftp ftps(InetAddress host, int port, String user, String pass) throws IOException {
        return new Ftps(host, port, user, pass, true);
    }
    
    /**
     * open ftps
     * @param host
     * ip or domain
     * @param port
     * port (ftps basic port 990)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    static Ftp ftps(String host, int port, String user, String pass) throws IOException {
        return new Ftps(InetAddress.getByName(host), port, user, pass, true);
    }
    
    /**
     * open sftp
     * @param host
     * ip or domain
     * @param port
     * port (sftp basic port 22)
     * @param user
     * username
     * @param pass
     * password
     * @return
     * FTP Object
     * @throws IOException
     */
    static Ftp sftp(String host, int port, String user, String pass) throws IOException {
        return new Sftp(host, port, user, pass);
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
     * @param directoryname
     * @return
     * @throws IOException
     */
    boolean hasDirectory(String directoryname) throws IOException;
    
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
