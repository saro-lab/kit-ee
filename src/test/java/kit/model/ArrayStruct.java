package kit.model;

import me.saro.kit.bytes.fixed.annotations.BinaryData;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;

import java.util.Arrays;
import java.util.List;

@FixedDataClass(size=40)
public class ArrayStruct {

    @BinaryData(offset=0)
    int member1;

    @BinaryData(offset=4, arrayLength=4)
    int[] member2;

    @BinaryData(offset=20, arrayLength=2)
    List<Long> member3;

    @BinaryData(offset=36, arrayLength=2)
    Short[] member4;

    public ArrayStruct() {
    }

    public ArrayStruct(int member1, int[] member2, List<Long> member3, Short[] member4) {
        this.member1 = member1;
        this.member2 = member2;
        this.member3 = member3;
        this.member4 = member4;
    }

    public int getMember1() {
        return member1;
    }

    public void setMember1(int member1) {
        this.member1 = member1;
    }

    public int[] getMember2() {
        return member2;
    }

    public void setMember2(int[] member2) {
        this.member2 = member2;
    }

    public List<Long> getMember3() {
        return member3;
    }

    public void setMember3(List<Long> member3) {
        this.member3 = member3;
    }

    public Short[] getMember4() {
        return member4;
    }

    public void setMember4(Short[] member4) {
        this.member4 = member4;
    }

    @Override
    public String toString() {
        return "ArrayStruct{" +
                "member1=" + member1 +
                ", member2=" + Arrays.toString(member2) +
                ", member3=" + member3 +
                ", member4=" + Arrays.toString(member4) +
                '}';
    }
}