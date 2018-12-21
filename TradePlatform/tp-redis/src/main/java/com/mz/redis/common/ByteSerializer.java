package com.mz.redis.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by Frank on 2018-11-30.
 */
public class ByteSerializer implements RedisSerializer {

    private final static Logger logger = LoggerFactory.getLogger(ByteSerializer.class);

    /**
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            logger.error("redis序列化对象失败", ex);
        }
        return bytes;
    }

    /**
     * @param byteArray
     * @param clazz
     * @return
     * @see com.mz.redis.common.RedisSerializer#deserialize(byte[], Class)
     */
    @Override
    public <T> T deserialize(byte[] byteArray, Class<T> clazz) {
        Object obj;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
            return (T) obj;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param byteArray
     * @param itemClazz
     * @return
     * @see com.mz.redis.common.RedisSerializer#deserializeForList(byte[], Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> deserializeForList(byte[] byteArray, Class<E> itemClazz) {
        Object obj;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
            return (List<E>) obj;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
