package kit;

import me.saro.kit.model.SerialTextData;
import org.junit.jupiter.api.Test;

public class ClassSerializerTest {

    @Test
    public void textData() throws Exception {

        var data =
                "홍길동 "+
                        "-23       "+
                        "10        "+
                        "-312      "+
                        "9423412347";

        System.out.println(data);


        var textData = new SerialTextData().read(data);

        System.out.println(textData);
        System.out.println(textData.toSerializeString());

        assertEquals(textData.toSerializeString(), data);
    }

}
