package me.saro.kit.bytes;

import me.saro.kit.Texts;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

/**
 * byte data
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public class ByteData {

    final private String charset;
    final private ByteBuffer byteBuffer;

    /**
     *
     * @param data
     * @param charset
     */
    public ByteData(String data, String charset) {
        byteBuffer = ByteBuffer.wrap(Texts.getBytes(data, charset));
        this.charset = charset;
    }

    /**
     *
     * @param data
     * @param charset
     */
    public ByteData(byte[] data, String charset) {
        this.byteBuffer = ByteBuffer.wrap(data);
        this.charset = charset;
    }

    /**
     *
     * @param byteSize
     * @param charset
     */
    public ByteData(int byteSize, String charset) {
        this.byteBuffer = ByteBuffer.allocate(byteSize);
        this.charset = charset;
    }

    /**
     *
     * @return
     */
    public int limit() {
        return this.byteBuffer.limit();
    }

    /**
     *
     * @return
     */
    public int position() {
        return this.byteBuffer.position();
    }

    /**
     *
     * @param tempPosition
     * @param run
     * @param <R>
     * @return
     */
    private <R> R runOutsidePosition(int tempPosition, Supplier<R> run) {
        var prevPosition = byteBuffer.position();
        byteBuffer.position(tempPosition);
        var value = run.get();
        byteBuffer.position(prevPosition);
        return value;
    }

    /**
     *
     * @return
     */
    public byte readByte() {
        return this.byteBuffer.get();
    }

    /**
     *
     * @return
     */
    public short readShort() {
        return byteBuffer.getShort();
    }

    /**
     *
     * @return
     */
    public int readInt() {
        return byteBuffer.getInt();
    }

    /**
     *
     * @return
     */
    public long readLong() {
        return byteBuffer.getLong();
    }

    /**
     *
     * @return
     */
    public float readFloat() {
        return byteBuffer.getFloat();
    }

    /**
     *
     * @return
     */
    public double readDouble() {
        return byteBuffer.getDouble();
    }

    /**
     *
     * @param size
     * @return
     */
    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        this.byteBuffer.get(bytes);
        return bytes;
    }

    /**
     *
     * @param size
     * @return
     */
    public String readString(int size) {
        return Bytes.toString(readBytes(size), charset);
    }

    /**
     *
     * @param size
     * @return
     */
    public String readRtrimString(int size) {
        return readRtrimString(size, ' ');
    }

    /**
     *
     * @param size
     * @param spaceCharacter
     * @return
     */
    public String readRtrimString(int size, char spaceCharacter) {
        return Texts.rtrim(readString(size), spaceCharacter);
    }

    /**
     *
     * @return
     */
    public byte readByte(int position) {
        return runOutsidePosition(position, () -> byteBuffer.get());
    }

    /**
     *
     * @return
     */
    public short readShort(int position) {
        return runOutsidePosition(position, () -> byteBuffer.getShort());
    }

    /**
     *
     * @return
     */
    public int readInt(int position) {
        return runOutsidePosition(position, () -> byteBuffer.getInt());
    }

    /**
     *
     * @return
     */
    public long readLong(int position) {
        return runOutsidePosition(position, () -> byteBuffer.getLong());
    }

    /**
     *
     * @return
     */
    public float readFloat(int position) {
        return runOutsidePosition(position, () -> byteBuffer.getFloat());
    }

    /**
     *
     * @return
     */
    public double readDouble(int position) {
        return runOutsidePosition(position, () -> byteBuffer.getDouble());
    }

    /**
     *
     * @param size
     * @return
     */
    public byte[] readBytes(int position, int size) {
        return runOutsidePosition(position, () -> readBytes(size));
    }

    /**
     *
     * @param size
     * @return
     */
    public String readString(int position, int size) {
        return runOutsidePosition(position, () -> readString(size));
    }

    /**
     *
     * @param size
     * @param spaceCharacter
     * @return
     */
    public String readRtrimString(int position, int size, char spaceCharacter) {
        return runOutsidePosition(position, () -> readString(size, spaceCharacter));
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteData writeByte(byte b) {
        byteBuffer.put(b);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData writeShort(short value) {
        byteBuffer.putShort(value);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData writeInt(int value) {
        byteBuffer.putInt(value);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData writeLong(long value) {
        byteBuffer.putLong(value);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData writeFloat(float value) {
        byteBuffer.putFloat(value);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData writeDouble(double value) {
        byteBuffer.putDouble(value);
        return this;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public ByteData writeBytes(byte[] bytes) {
        if (bytes != null) {
            byteBuffer.put(bytes);
        }
        return this;
    }

    /**
     *
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public ByteData writeBytes(byte[] bytes, int offset, int length) {
        if (bytes != null) {
            byteBuffer.put(bytes, offset, length);
        }
        return this;
    }

    /**
     *
     * @param b
     * @param repeatSize
     * @return
     */
    public ByteData writeRepeatByte(byte b, int repeatSize) {
        for (int i = 0 ; i < repeatSize ; i++) {
            byteBuffer.put(b);
        }
        return this;
    }

    /**
     *
     * @param text
     * @return
     */
    public ByteData writeString(String text) {
        if (text != null) {
            byteBuffer.put(Texts.getBytes(text, charset));
        }
        return this;
    }

    /**
     *
     * @param text
     * @param fixedSize
     * @return
     */
    public ByteData writeString(String text, int fixedSize) {
        return writeString(text, fixedSize, (byte)' ');
    }

    /**
     *
     * @param text
     * @param fixedSize
     * @param fillByte
     * @return
     */
    public ByteData writeString(String text, int fixedSize, byte fillByte) {
        if (text == null) {
            text = "";
        }
        byte[] bytes = Texts.getBytes(text, charset);
        if (bytes.length > fixedSize) {
            throw new ArrayIndexOutOfBoundsException("`" + text + "` is over " + fixedSize + " bytes");
        }
        byteBuffer.put(bytes);
        if (bytes.length < fixedSize) {
            writeRepeatByte(fillByte, fixedSize - bytes.length);
        }
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public ByteData insertByte(int position, byte b) {
        return runOutsidePosition(position, () -> writeByte(b));
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData insertShort(int position, short value) {
        return runOutsidePosition(position, () -> writeShort(value));
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData insertInt(int position, int value) {
        return runOutsidePosition(position, () -> writeInt(value));
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData insertLong(int position, long value) {
        return runOutsidePosition(position, () -> writeLong(value));
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData insertFloat(int position, float value) {
        return runOutsidePosition(position, () -> writeFloat(value));
    }

    /**
     *
     * @param value
     * @return
     */
    public ByteData insertDouble(int position, double value) {
        return runOutsidePosition(position, () -> writeDouble(value));
    }

    /**
     *
     * @param bytes
     * @return
     */
    public ByteData insertBytes(int position, byte[] bytes) {
        return runOutsidePosition(position, () -> writeBytes(bytes));
    }

    /**
     *
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public ByteData insertBytes(int position, byte[] bytes, int offset, int length) {
        return runOutsidePosition(position, () -> writeBytes(bytes, offset, length));
    }

    /**
     *
     * @param b
     * @param repeatSize
     * @return
     */
    public ByteData writeRepeatByte(int position, byte b, int repeatSize) {
        return runOutsidePosition(position, () -> writeRepeatByte(b, repeatSize));
    }

    /**
     *
     * @param text
     * @return
     */
    public ByteData insertString(int position, String text) {
        return runOutsidePosition(position, () -> writeString(text));
    }

    /**
     *
     * @param position
     * @param text
     * @param fixedSize
     * @return
     */
    public ByteData insertString(int position, String text, int fixedSize) {
        return runOutsidePosition(position, () -> writeString(text, fixedSize));
    }

    /**
     *
     * @param position
     * @param text
     * @param fixedSize
     * @param fillByte
     * @return
     */
    public ByteData insertString(int position, String text, int fixedSize, byte fillByte) {
        return runOutsidePosition(position, () -> writeString(text, fixedSize, fillByte));
    }

    /**
     *
     * @param size
     * @return
     */
    public byte readTextByte(int size) {
        return Bytes.parseByte(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param size
     * @return
     */
    public short readTextShort(int size) {
        return Short.parseShort(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param size
     * @return
     */
    public int readTextInt(int size) {
        return Integer.parseInt(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param size
     * @return
     */
    public long readTextLong(int size) {
        return Long.parseLong(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param size
     * @return
     */
    public float readTextFloat(int size) {
        return Float.parseFloat(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param size
     * @return
     */
    public double readTextDouble(int size) {
        return Double.parseDouble(readString(size).replaceAll("[^0-9^\\.^\\-]+", ""));
    }

    /**
     *
     * @param value
     * @param size
     * @return
     */
    public ByteData writeTextByte(byte value, int size) {
        return writeTextByte(value, size, (byte)' ');
    }

    /**
     *
     * @param value
     * @param size
     * @param fillByte
     * @return
     */
    public ByteData writeTextByte(byte value, int size, byte fillByte) {
        return writeString(Byte.toString(value), size, fillByte);
    }

    /**
     *
     * @param value
     * @param size
     * @return
     */
    public ByteData writeTextShort(short value, int size) {
        return writeTextShort(value, size, (byte)' ');
    }

    /**
     *
     * @param value
     * @param size
     * @param fillByte
     * @return
     */
    public ByteData writeTextShort(short value, int size, byte fillByte) {
        return writeString(Short.toString(value), size, fillByte);
    }

    /**
     *
     * @param value
     * @param size
     * @return
     */
    public ByteData writeTextInt(int value, int size) {
        return writeTextInt(value, size, (byte)' ');
    }

    /**
     *
     * @param value
     * @param size
     * @param fillByte
     * @return
     */
    public ByteData writeTextInt(int value, int size, byte fillByte) {
        return writeString(Integer.toString(value), size, fillByte);
    }

    /**
     *
     * @param value
     * @param size
     * @return
     */
    public ByteData writeTextLong(long value, int size) {
        return writeTextLong(value, size, (byte)' ');
    }

    /**
     *
     * @param value
     * @param size
     * @param fillByte
     * @return
     */
    public ByteData writeTextLong(long value, int size, byte fillByte) {
        return writeString(Long.toString(value), size, fillByte);
    }

    /**
     *
     * @return
     */
    public byte[] toBytes() {
        byte[] data = new byte[byteBuffer.position()];
        byteBuffer.compact();
        byteBuffer.get(data);
        return data;
    }
}
