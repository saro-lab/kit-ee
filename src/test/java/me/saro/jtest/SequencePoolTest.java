package me.saro.jtest;

import me.saro.kit.db.SequencePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("SequencePool Test")
public class SequencePoolTest {

    long seq = 1;

    public List<Long> getSeqList(int size) {
        return Stream.of(new Long[size]).map(e -> seq++).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    @DisplayName("max 1")
    public void test1() throws Exception {
        SequencePool<Long> pool = new SequencePool.Builder()
                .minPoolSize(1).maxPoolSize(1)
                .build();

        System.out.println("create pool: " + pool);
        for (var i = 0 ; i < 100 ; i++) {
            System.out.println(pool.get(this::getSeqList) + " " + pool);
        }
        Assertions.assertEquals(pool.getNowPoolSize(), 1);
    }

    @DisplayName("max 100 - up and down")
    public void test2() throws Exception {
        SequencePool<Long> pool = new SequencePool.Builder()
                .minPoolSize(1).maxPoolSize(10)
                .build();

        System.out.println("create pool: " + pool);
        for (var i = 0 ; i < 100 ; i++) {
            System.out.println(pool.get(this::getSeqList) + " " + pool);
        }
        Assertions.assertEquals(pool.getNowPoolSize(), 10);

        System.out.println("pool down test");

        for (var i = 0 ; i < 20 ; i++) {
            pool.resetLastIssuedTime();
            System.out.println(pool.get(this::getSeqList) + " " + pool);
        }

        Assertions.assertEquals(pool.getNowPoolSize(), 1);
    }

    @DisplayName("count")
    public void test3() throws Exception {
        Assertions.assertEquals(seq, 221);
    }
}
