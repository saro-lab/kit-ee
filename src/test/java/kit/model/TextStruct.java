package kit.model;

import me.saro.kit.bytes.fixed.annotations.FixedDataClass;
import me.saro.kit.bytes.fixed.annotations.TextData;
import me.saro.kit.bytes.fixed.annotations.TextDataAlign;

@FixedDataClass(size=100, fill=0, charset="UTF-8")
public class TextStruct {

    @TextData(offset=0, length=3, unsigned=true)
    byte byteData;

    @TextData(offset=3, length=7)
    Short shortData;

    @TextData(offset=10, length=10, radix=16, fill='0', align= TextDataAlign.right)
    int intData;

    @TextData(offset=20, length=20)
    long longData;

    @TextData(offset=40, length=20)
    float floatData;

    @TextData(offset=60, length=20)
    double doubleData;

    @TextData(offset=80, length=10)
    String leftText;

    @TextData(offset=90, length=10, align=TextDataAlign.right)
    String rightText;

    public TextStruct() {
    }

    public TextStruct(byte byteData, Short shortData, int intData, long longData, float floatData, double doubleData, String leftText, String rightText) {
        this.byteData = byteData;
        this.shortData = shortData;
        this.intData = intData;
        this.longData = longData;
        this.floatData = floatData;
        this.doubleData = doubleData;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    public byte getByteData() {
        return byteData;
    }

    public void setByteData(byte byteData) {
        this.byteData = byteData;
    }

    public Short getShortData() {
        return shortData;
    }

    public void setShortData(Short shortData) {
        this.shortData = shortData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }

    public long getLongData() {
        return longData;
    }

    public void setLongData(long longData) {
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

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    @Override
    public String toString() {
        return "TextStruct{" +
                "byteData=" + byteData +
                ", shortData=" + shortData +
                ", intData=" + intData +
                ", longData=" + longData +
                ", floatData=" + floatData +
                ", doubleData=" + doubleData +
                ", leftText='" + leftText + '\'' +
                ", rightText='" + rightText + '\'' +
                '}';
    }
}
