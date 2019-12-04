package kit;

import me.saro.kit.bytes.Bytes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytesTest {
    
    @Test
    public void base64() {
        String sample = "가나다abcd";
        String encode = Bytes.encodeBase64String(sample, "UTF-8");
        String decode = Bytes.decodeBase64(encode, "UTF-8");
        assertEquals(sample, decode);
    }
    
    @Test
    public void toBytesByHex() {
        String sample = "fff0fa32713281af";
        assertEquals(Bytes.toHex(Bytes.toBytesByHex(sample)), sample);
        assertEquals(Bytes.toHex(Bytes.toBytesByHex("FFff")), "ffff");
        assertEquals(Bytes.toHex(Bytes.toBytesByHex("ffFF")), "ffff");
    }
    
    @Test
    public void shortTest() {
        assertEquals(Bytes.toHex(Bytes.toBytes((short)32)), "0020");
        assertEquals(Bytes.toHex(Bytes.toBytes((short)Short.parseShort("-1"))), "ffff");
        assertEquals(Bytes.toShort(Bytes.toBytes((short)123)), (short)123);
    }
    
    @Test
    public void intTest() {
        assertEquals(Bytes.toHex(Bytes.toBytes((int)32)), "00000020");
        assertEquals(Bytes.toHex(Bytes.toBytes((int)-1)), "ffffffff");
        assertEquals(Bytes.toInt(Bytes.toBytes(8080)), 8080);
    }
    
    @Test
    public void longTest() {
        assertEquals(Bytes.toHex(Bytes.toBytes((long)32)), "0000000000000020");
        assertEquals(Bytes.toHex(Bytes.toBytes(-1L)), "ffffffffffffffff");
        assertEquals(Bytes.toLong(Bytes.toBytes(324L)), 324L);
    }
    
    @Test
    public void floatTest() {
        float value = 32;
        byte[] bytes = Bytes.toBytes(value);
        float rocv = Bytes.toFloat(bytes);
        assertEquals(value, rocv);
    }
    
    @Test
    public void dobuleTest() {
        double value = 32;
        byte[] bytes = Bytes.toBytes(value);
        double rocv = Bytes.toDouble(bytes);
        assertEquals(value, rocv);
    }
}
