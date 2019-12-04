package kit;


import me.saro.kit.bytes.ByteData;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteDataTest {
    
    final static byte SPACE = ' ';
    
    @Test
    public void create() throws IOException {
        assertEquals(new ByteData(10, "UTF-8").writeString("hello").position(), 5);
        assertEquals(new ByteData(15, "UTF-8").writeString("안녕하세요").position(), 15);
    }

}
