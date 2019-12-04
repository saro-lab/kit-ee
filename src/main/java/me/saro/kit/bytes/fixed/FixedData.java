package me.saro.kit.bytes.fixed;


import me.saro.kit.Texts;
import me.saro.kit.bytes.Bytes;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Fixed Data Mapper
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public interface FixedData {
    
    Map<Class<?>, FixedData> STORE = new HashMap<>();
    
    /**
     * get instance
     * @param clazz
     * @return
     */
    static FixedData getInstance(Class<?> clazz) {
        FixedData mapper;
        synchronized (STORE) {
            mapper = STORE.get(clazz);
            if (mapper == null) {
                STORE.put(clazz, mapper = new FixedDataImpl(clazz));
            }
        }
        return mapper;
    }
    
    /**
     * meta info
     * @return
     */
    FixedDataClass meta();
    
    /**
     * get class
     * @return
     */
    Class<?> getTargetClass();
    
    /**
     * bytes to class
     * @param bytes
     * @param offset
     * @return
     */
    <T> T toClass(byte[] bytes, int offset);
  
    /**
     * the class to bind(write) the bytes
     * @param data
     * @param out
     * @param offset
     * @return
     */
    byte[] bindBytes(Object data, byte[] out, int offset);
    
    /**
     * bytes to class
     * @param bytes
     * @return
     */
    default <T> T toClass(byte[] bytes) {
        return toClass(bytes, 0);
    }

    /**
     * to class
     * @param data
     * @param <T>
     * @return
     */
    default <T> T toClass(String data) {
        return toClass(Texts.getBytes(data, meta().charset()), 0);
    }

    /**
     * to class
     * @param data
     * @param <T>
     * @return
     * @throws IOException
     */
    default <T> T toClassWithCheckByte(String data) throws IOException {
        byte[] buf = data.getBytes(meta().charset());
        if (buf.length != meta().size()) {
            throw new IOException(
                "size incorrect " + getTargetClass().getName() + " size is " + meta().size() + "byte " +
                "but data size "+buf.length+", data info [" + meta().charset() + "][" + data + "]"
            );
        }
        return toClass(buf, 0);
    }
    
    /**
     * class to bytes
     * @param data
     * @return
     */
    default byte[] toBytes(Object data) {
        return bindBytes(data, new byte[meta().size()]);
    }

    /**
     * class to String
     * @param data
     * @return
     */
    default String toString(Object data) {
        return Bytes.toString(toBytes(data), meta().charset());
    }
    
    /**
     * the class to bind(write) the bytes
     * @param data
     * @param out
     * @return
     */
    default byte[] bindBytes(Object data, byte[] out) {
        return bindBytes(data, out, 0);
    }
    
    /**
     * the class to bind(write) the outputstream<br>
     * is not close
     * @param data
     * @param out
     * @return
     * @throws IOException
     */
    default OutputStream bindBytes(Object data, OutputStream out) throws IOException {
        out.write(toBytes(data));
        out.flush();
        return out;
    }
}
