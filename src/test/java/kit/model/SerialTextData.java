package kit.model;

import me.saro.kit.bytes.ByteData;
import me.saro.kit.bytes.variable.ClassSerializer;

public class SerialTextData extends ClassSerializer<SerialTextData> {

    String text;
    byte textByte;
    short textShort;
    int textInt;
    long textLong;

    @Override
    protected void read(ByteData data) {
        setText(data.readString(10));
        setTextByte(data.readTextByte(10));
        setTextShort(data.readTextShort(10));
        setTextInt(data.readTextInt(10));
        setTextLong(data.readTextLong(10));
    }

    @Override
    protected void write(ByteData data) {
        data.writeString(getText(), 10);
        data.writeTextByte(getTextByte(), 10);
        data.writeTextShort(getTextShort(), 10);
        data.writeTextInt(getTextInt(), 10);
        data.writeTextLong(getTextLong(), 10);
    }

    @Override
    protected int limitSizeByteData() {
        return 50;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte getTextByte() {
        return textByte;
    }

    public void setTextByte(byte textByte) {
        this.textByte = textByte;
    }

    public short getTextShort() {
        return textShort;
    }

    public void setTextShort(short textShort) {
        this.textShort = textShort;
    }

    public int getTextInt() {
        return textInt;
    }

    public void setTextInt(int textInt) {
        this.textInt = textInt;
    }

    public long getTextLong() {
        return textLong;
    }

    public void setTextLong(long textLong) {
        this.textLong = textLong;
    }

    @Override
    public String toString() {
        return "SerialTextData{" +
                "text='" + text + '\'' +
                ", textByte=" + textByte +
                ", textShort=" + textShort +
                ", textInt=" + textInt +
                ", textLong=" + textLong +
                '}';
    }
}
