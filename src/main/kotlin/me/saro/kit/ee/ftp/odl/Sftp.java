package me.saro.kit.ee.ftp.odl;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import me.saro.kit.ee.ftp.Ftp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * SFTP
 * it is not FTPS
 * SFTP != FTPS
 */
public class Sftp implements Ftp {

    final ChannelSftp sftp;
    final Session session;

    public Sftp(String host, int port, String user, SftpOpener opener) throws IOException {
        try {
            sftp = opener.open((session = new JSch().getSession(user, host, port)));
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public boolean path(String pathname) throws IOException {
        try {
            sftp.cd(pathname);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public String path() throws IOException {
        try {
            return sftp.pwd();
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
    
    private List<String> list(Predicate<LsEntry> filter) throws IOException {
        try {
            List<String> list = new ArrayList<String>();
            sftp.ls(path(), e -> {
                if (filter.test(e)) {
                    list.add(e.getFilename());
                }
                return LsEntrySelector.CONTINUE;
            });
            return list;
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public List<String> listFiles(Predicate<String> filter) throws IOException {
        return list(e -> !e.getAttrs().isDir() && filter.test(e.getFilename()));
    }
    
    @Override
    public List<String> listFiles() throws IOException {
        return list(e -> !e.getAttrs().isDir());
    }

    @Override
    public List<String> listDirectories(Predicate<String> filter) throws IOException {
        return list(e -> e.getAttrs().isDir() && !e.getFilename().matches("[\\.]{1,2}") && filter.test(e.getFilename()));
    }
    
    @Override
    public List<String> listDirectories() throws IOException {
        return list(e -> e.getAttrs().isDir() && !e.getFilename().matches("[\\.]{1,2}"));
    }
    
    @Override
    public boolean hasFile(String filename) throws IOException {
        try {
            return Optional.ofNullable(sftp.lstat(path() + "/" + filename))
                    .filter(e -> !e.isDir()).isPresent();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public boolean hasDirectory(String directoryName) throws IOException {
        try {
            return Optional.ofNullable(sftp.lstat(path() + "/" + directoryName))
                    .filter(e -> e.isDir()).isPresent();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            throw new IOException(e);
        }
    }

    @Override
    public boolean delete(String filename) throws IOException {
        try {
            if (hasFile(filename)) {
                sftp.rm(filename);
                return true;
            } else if (hasDirectory(filename)) {
                sftp.rmdir(filename);
                return true;
            }
           return false;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean send(String saveFilename, File localFile) throws IOException {
        try (InputStream input = new FileInputStream(localFile)) {
            sftp.put(input, saveFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean recv(String remoteFilename, File localFile) throws IOException {
        if (!hasFile(remoteFilename)) {
            return false;
        }
        if (localFile.exists()) {
            localFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            sftp.get(remoteFilename, fos);
            return true;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean mkdir(String createDirectoryName) throws IOException {
        try {
            sftp.mkdir(createDirectoryName);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            sftp.disconnect();
        } catch (Exception e) {
        }
        try {
            session.disconnect();
        } catch (Exception e) {
        }
    }

    public ChannelSftp getChannelSftp() {
        return sftp;
    }
    public Session getSession() {
        return session;
    }

    public static interface SftpOpener {
        ChannelSftp open(final Session session) throws IOException, JSchException;
    }
}
