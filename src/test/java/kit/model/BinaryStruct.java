package kit.model;

import me.saro.kit.bytes.fixed.annotations.BinaryData;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;

import java.util.Arrays;

@FixedDataClass(size=30, fill=0)
public class BinaryStruct {

    @BinaryData(offset=0)
    byte byteData;

    @BinaryData(offset=1)
    short shortData;

    @BinaryData(offset=3)
    int intData;

    @BinaryData(offset=7)
    Long longData; // test long -> Long

    @BinaryData(offset=15)
    float floatData;

    @BinaryData(offset=19)
    double doubleData;

    @BinaryData(offset=27, arrayLength=3)
    byte[] bytesData;

    public BinaryStruct() {
    }

    public BinaryStruct(byte byteData, short shortData, int intData, Long longData, float floatData, double doubleData, byte[] bytesData) {
        this.byteData = byteData;
        this.shortData = shortData;
        this.intData = intData;
        this.longData = longData;
        this.floatData = floatData;
        this.doubleData = doubleData;
        this.bytesData = bytesData;
    }

    public byte getByteData() {
        return byteData;
    }

    public void setByteData(byte byteData) {
        this.byteData = byteData;
    }

    public short getShortData() {
        return shortData;
    }

    public void setShortData(short shortData) {
        this.shortData = shortData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }

    public Long getLongData() {
        return longData;
    }

    public void setLongData(Long longData) {
        this.longData = longData;
    }

    public float getFloatData() {
        return floatData;
    }

    public void setFloatData(float floatData) {
        this.floatData = floatData;
    }

    public double getDoubleData() {
        return doubleData;
    }

    public void setDoubleData(double doubleData) {
        this.doubleData = doubleData;
    }

    public byte[] getBytesData() {
        return bytesData;
    }

    public void setBytesData(byte[] bytesData) {
        this.bytesData = bytesData;
    }

    @Override
    public String toString() {
        return "BinaryStruct{" +
                "byteData=" + byteData +
                ", shortData=" + shortData +
                ", intData=" + intData +
                ", longData=" + longData +
                ", floatData=" + floatData +
                ", doubleData=" + doubleData +
                ", bytesData=" + Arrays.toString(bytesData) +
                '}';
    }
}
