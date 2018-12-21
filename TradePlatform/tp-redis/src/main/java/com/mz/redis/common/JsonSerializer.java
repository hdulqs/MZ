package com.mz.redis.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.util.SafeEncoder;

import java.util.List;

/**
 * Created by Frank on 2018-11-30.
 */
public class JsonSerializer implements RedisSerializer {
    private final static Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private final static SerializerFeature[] feature = {SerializerFeature.UseISO8601DateFormat, SerializerFeature.WriteClassName};

    @Override
    public byte[] serialize(Object object) {
        if (object == null)
            return null;
        try {
            return SafeEncoder.encode(JSONObject.toJSONString(object, feature));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    @Override
    public <T> T deserialize(byte[] byteArray, Class<T> clazz) {
        if (byteArray == null || byteArray.length == 0)
            return null;
        try {
            return JSONObject.parseObject(SafeEncoder.encode(byteArray), clazz);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    @Override
    public <E> List<E> deserializeForList(byte[] byteArray, Class<E> itemClazz) {
        if (byteArray == null || byteArray.length == 0)
            return null;
        try {
            return JSONObject.parseArray(SafeEncoder.encode(byteArray), itemClazz);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }
}
