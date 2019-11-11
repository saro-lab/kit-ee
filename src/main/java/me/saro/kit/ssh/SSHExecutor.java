package me.saro.kit.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import me.saro.commons.__old.bytes.Utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SSH<br>
 * there is just execute application and output in session<br>
 * recommend use the {@link SSHShell}
 * @since 1.2
 */
public class SSHExecutor implements Closeable {
    
    final Session session;
    final String charset;
    
    /**
     * ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @throws IOException
     */
    private SSHExecutor(String host, int port, String user, String pass, String charset) throws IOException {
        try {
            this.charset = charset;
            session = new JSch().getSession(user, host, port);
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();;
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    /**
     * open ssh
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @return
     * @throws IOException
     */
    public static SSHExecutor open(String host, int port, String user, String pass, String charset) throws IOException {
        return new SSHExecutor(host, port, user, pass, charset);
    }
    
    /**
     * send just commend and return result
     * @param host
     * @param port
     * @param user
     * @param pass
     * @param charset
     * @param cmd
     * @return
     * @throws IOException
     */
    public static String just(String host, int port, String user, String pass, String charset, String... cmds) throws IOException {
        String rv;
        try (SSHExecutor ssh = SSHExecutor.open(host, port, user, pass, charset)) {
            rv = ssh.cmd(cmds);
        }
        return rv;
    }
    
    /**
     * send commend
     * @param cmd
     * @return
     * @throws IOException
     */
    public synchronized String cmd(String... cmds) throws IOException {
        
        String rv = null;
        ChannelExec channel = null;
        
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(Optional.ofNullable(cmds)
                    .filter(e -> e.length > 0).map(Stream::of).map(e -> e.collect(Collectors.joining("\n", "", "\n")))
                    .orElseThrow(() -> new IllegalArgumentException("there is no command")).getBytes(charset));
            channel.connect();
            try (InputStream is = channel.getInputStream() ; InputStream eis = channel.getErrStream()) {
                rv  = Utils.nvl(Utils.bvl(read(is), read(eis)), "");
            }
        } catch (JSchException je) {
            throw new IOException(je);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                }
            }
        }
        
        return rv;
    }
    
    /**
     * read cmd
     * @return
     * @throws IOException 
     */
    private String read(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        int ch;
        while ((ch = is.read()) != -1) {
            baos.write(ch);
        }
        String rv = baos.toString(charset);
        return rv;
    }
    
    /**
     * close
     */
    @Override
    public void close() throws IOException {
        try {
            session.disconnect();
        } catch (Exception e) {
        }
    }
}
