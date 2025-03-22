package me.saro.jtest;

import me.saro.kit.i18n.I18n;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

@DisplayName("I18n Test")
public class I18nTest {
    @Test
    public void test1() {

        var dir = new File(me.saro.ktest.I18nTest.class.getClassLoader().getResource("lang").getFile());
        var i18n = I18n.load(dir);

        var en = i18n.locale("en");
        var ko = i18n.locale(List.of("ko", "en"));
        var du = i18n.locale("du");
        var duKo = i18n.locale(List.of("du", "ko"));

        Assertions.assertEquals(en.get("success"), "Success");
        Assertions.assertEquals(ko.get("success"), "성공");
        Assertions.assertEquals(ko.get("success.abc"), "success.abc");
        Assertions.assertEquals(du.get("success"), "Success");

        Assertions.assertEquals(duKo.get("exception"), "예외");

    }
}
