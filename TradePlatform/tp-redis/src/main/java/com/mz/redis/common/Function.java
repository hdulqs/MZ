package com.mz.redis.common;

/**
 * Created by Frank on 2018-11-29.
 */
public interface Function<T, E> {
    T callback(E e);
}
