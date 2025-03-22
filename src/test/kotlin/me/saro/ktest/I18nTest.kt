package me.saro.ktest

import me.saro.kit.i18n.I18n.Companion.load
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File

@DisplayName("I18n Test")
class I18nTest {
    @Test
    fun test1() {
        val dir = File(I18nTest::class.java.classLoader.getResource("lang")?.file ?: "")
        val i18n = load(dir)

        val en = i18n.locale("en")
        val ko = i18n.locale(listOf("ko", "en"))
        val du = i18n.locale("du")
        val duKo = i18n.locale(listOf("du", "ko"))

        Assertions.assertEquals(en["success"], "Success")
        Assertions.assertEquals(ko["success"], "성공")
        Assertions.assertEquals(ko["success.abc"], "success.abc")
        Assertions.assertEquals(du["success"], "Success")

        Assertions.assertEquals(duKo["exception"], "예외")
    }
}
