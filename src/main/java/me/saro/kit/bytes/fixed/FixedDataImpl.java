package me.saro.kit.bytes.fixed;


import me.saro.kit.bytes.fixed.annotations.FixedDataClass;
import me.saro.kit.functions.ThrowableConsumer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Fixed Data Mapper Impl
 * @author PARK Yong Seo
 * @since 1.0.0
 */
class FixedDataImpl implements FixedData {

    final Class<?> clazz;
    final FixedDataClass fixedDataClassInfo;
    
    Constructor<?> constructor;
    List<FixedMethodConsumer> toBytesConsumers;
    List<FixedMethodConsumer> toClassConsumers;
    
    FixedDataImpl(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not null !!");
        }
        
        this.clazz = clazz;
        this.fixedDataClassInfo = clazz.getDeclaredAnnotation(FixedDataClass.class);
        init();
    }
    
    private void init() {
        if (fixedDataClassInfo == null) {
            throw new IllegalArgumentException(clazz.getName() + " is not defined @FixedDataClass");
        }
        
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                this.constructor = constructor;
                break;
            }
        }
        if (this.constructor == null) {
            throw new IllegalArgumentException(clazz.getName() + " does not have default Constructor");
        }
        
        // get field have data annotation
        Field[] fields = clazz.getDeclaredFields();
        toBytesConsumers = FixedMethodUtils.toBytesConsumers(fixedDataClassInfo, clazz, fields);
        toClassConsumers = FixedMethodUtils.toClassConsumers(fixedDataClassInfo, clazz, fields);
    }
    
    @Override
    public FixedDataClass meta() {
        return fixedDataClassInfo;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return clazz;
    }

    @Override
    public byte[] bindBytes(Object data, byte[] out, int offset) {
        toBytesConsumers.parallelStream().forEach(ThrowableConsumer.wrap(e -> e.to(out, offset, data)));
        return out;
    }

    @Override
    public <T> T toClass(byte[] bytes, int offset) {
        try {
            T t = (T) constructor.newInstance();
            toClassConsumers.parallelStream().forEach(ThrowableConsumer.wrap(e -> e.to(bytes, offset, t)));
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }

    }
}
