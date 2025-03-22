package me.saro.jtest;

import me.saro.kit.cache.CacheStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CacheStore Test")
public class CacheStoreTest {
    @Test
    public void test() throws Exception {
        CacheStore<String, String> cs = new CacheStore<>(100);

        Assertions.assertEquals(cs.find("aa", (id) -> "bb"), "bb");

        Assertions.assertEquals(cs.find("aa", (id) -> "cc"), "bb");

        Assertions.assertEquals(cs.find("dd", (id) -> "cc"), "cc");
    }
}
