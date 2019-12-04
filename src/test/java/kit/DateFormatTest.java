package kit;

import me.saro.kit.dates.DateFormat;
import org.junit.jupiter.api.Test;

public class DateFormatTest {

    @Test
    public void test() throws Exception {

        assertEquals(DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"), DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"));
        assertNotEquals(DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"), DateFormat.parse("2018-09-24 21:53:00", "yyyy-MM-dd HH:mm:ss"));

    }

}
