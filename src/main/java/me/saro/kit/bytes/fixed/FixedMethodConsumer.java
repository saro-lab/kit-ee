package me.saro.kit.bytes.fixed;

import java.lang.reflect.InvocationTargetException;

/**
 * FixedMethodConsumer
 * @author PARK Yong Seo
 * @since 1.0.0
 */
@FunctionalInterface
public interface FixedMethodConsumer {
    void to(byte[] bytes, int idx, Object val) throws InvocationTargetException, IllegalAccessException;
}
