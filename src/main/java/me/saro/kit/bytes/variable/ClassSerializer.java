package me.saro.kit.bytes.variable;

import me.saro.kit.bytes.ByteData;
import me.saro.kit.bytes.Bytes;

/**
 * ClassSerializer
 * @param <T>
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public abstract class ClassSerializer<T> {

    public T read(String data) {
        read(new ByteData(data, charsetByteData()));
        return (T)this;
    }

    public T read(byte[] data) {
        read(new ByteData(data, charsetByteData()));
        return (T)this;
    }

    public byte[] toBytes() {
        ByteData data = new ByteData(limitSizeByteData(), charsetByteData());
        write(data);
        return data.toBytes();
    }

    public String toSerializeString() {
        return Bytes.toString(toBytes(), charsetByteData());
    }

    protected String charsetByteData() {
        return "UTF-8";
    }

    /**
     * read byteData
     * @param data
     */
    protected abstract void read(ByteData data);

    /**
     * write byteData
     * @param data
     */
    protected abstract void write(ByteData data);

    /**
     * limit size byte data
     * @return
     */
    protected abstract int limitSizeByteData();
}
