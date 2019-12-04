package kit;

import org.junit.jupiter.api.Test;

public class ValidsTest {

    @Test
    public void isMail() throws Exception {
        assertTrue(Valids.isMail("abc@saro.me", 64));
        assertTrue(Valids.isMail("abc@localhost.com", 64));
        assertTrue(Valids.isMail("a_-b@abc.com", 64));
        assertTrue(Valids.isMail("1a_-b@abc.com", 64));
        assertFalse(Valids.isMail("name@localhost", 64));
        assertFalse(Valids.isMail("name@localhost.m", 64));
        assertFalse(Valids.isMail("@localhost.com", 64));
    }
    
    @Test
    public void isDate() throws Exception {
        assertTrue(Valids.isDate("20101017", "yyyyMMdd"));
        assertTrue(Valids.isDate("20040229", "yyyyMMdd"));
        assertFalse(Valids.isDate("20050229", "yyyyMMdd"));
    }

}
