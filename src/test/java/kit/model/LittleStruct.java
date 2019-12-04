package kit.model;

import me.saro.kit.bytes.fixed.annotations.BinaryData;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;
import me.saro.kit.bytes.fixed.annotations.TextData;

@FixedDataClass(size=14, bigEndian=false, fill = 0)
public class LittleStruct {

    @BinaryData(offset=0)
    int no;

    @TextData(offset=4, length=10)
    String text;

    public LittleStruct() {
    }

    public LittleStruct(int no, String text) {
        this.no = no;
        this.text = text;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "LittleStruct{" +
                "no=" + no +
                ", text='" + text + '\'' +
                '}';
    }
}
