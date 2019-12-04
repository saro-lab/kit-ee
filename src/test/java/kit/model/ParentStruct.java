package kit.model;

import me.saro.kit.bytes.fixed.annotations.BinaryData;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;

import java.util.Arrays;
import java.util.List;

@FixedDataClass(size=74)
public class ParentStruct {

    @BinaryData(offset=0)
    ChildStruct ch;

    @BinaryData(offset=14, arrayLength=2)
    List<ChildStruct> chArr;

    @BinaryData(offset=42, arrayLength=2)
    ChildStruct[] chList;

    @BinaryData(offset=70)
    int main;

    public ParentStruct() {
    }

    public ParentStruct(ChildStruct ch, List<ChildStruct> chArr, ChildStruct[] chList, int main) {
        this.ch = ch;
        this.chArr = chArr;
        this.chList = chList;
        this.main = main;
    }

    public ChildStruct getCh() {
        return ch;
    }

    public void setCh(ChildStruct ch) {
        this.ch = ch;
    }

    public List<ChildStruct> getChArr() {
        return chArr;
    }

    public void setChArr(List<ChildStruct> chArr) {
        this.chArr = chArr;
    }

    public ChildStruct[] getChList() {
        return chList;
    }

    public void setChList(ChildStruct[] chList) {
        this.chList = chList;
    }

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "ParentStruct{" +
                "ch=" + ch +
                ", chArr=" + chArr +
                ", chList=" + Arrays.toString(chList) +
                ", main=" + main +
                '}';
    }
}