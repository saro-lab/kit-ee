package me.saro.kit.bytes.fixed;

import java.lang.reflect.Method;

/**
 * FixedField
 * @author PARK Yong Seo
 * @since 1.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toBytes(Method method);
    FixedMethodConsumer toClass(Method method);
}
