package kit.model;

import me.saro.kit.bytes.fixed.annotations.BinaryData;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;
import me.saro.kit.bytes.fixed.annotations.TextData;

@FixedDataClass(size=34, charset="UTF-8")
public class MixedStruct {

    @TextData(offset=0, length=15)
    String firstName;

    @TextData(offset=15, length=15)
    String lastName;

    @BinaryData(offset=30)
    int memberId;

    public MixedStruct() {
    }

    public MixedStruct(String firstName, String lastName, int memberId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "MixedStruct{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", memberId=" + memberId +
                '}';
    }
}
