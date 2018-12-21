package com.mz.redis.common;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Frank on 2018-11-30.
 */
public interface RedisSerializer {

    public final static Charset CHARSET = Charset.forName("UTF-8");

    byte[] serialize(Object object);

    <T> T deserialize(byte[] byteArray, Class<T> clazz);

    <E> List<E> deserializeForList(byte[] byteArray, Class<E> itemClazz);

}
