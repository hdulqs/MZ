package com.mz.redis.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

/**
 * Created by Frank on 2018-11-29.
 */
@Component
public class RedisUtils {

    Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    ShardedJedisPool shardedJedisPool;

    private RedisSerializer serializer;

    public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        try (ShardedJedis jedis = shardedJedisPool.getResource()) {
            return jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            throw e;
        }
    }

    public interface JedisAction<T> {
        T action(ShardedJedis jedis);
    }

    public String set(final String key, final String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    public String get(final String key) {
        return execute(jedis -> jedis.get(key));
    }

    public String echo(final String string) {
        return execute(jedis -> jedis.echo(string));
    }

    public Boolean exists(final String key) {
        return execute(jedis -> jedis.exists(key));
    }

    public String type(final String key) {
        return execute(jedis -> jedis.type(key));
    }

    public Long expire(final String key, final int seconds) {
        return execute(jedis -> jedis.expire(key, seconds));
    }

    public Long expireAt(final String key, final long unixTime) {
        return execute(jedis -> jedis.expireAt(key, unixTime));
    }

    public Long ttl(final String key) {
        return execute(jedis -> jedis.ttl(key));
    }

    public Boolean setbit(final String key, final long offset, final boolean value) {
        return execute(jedis -> jedis.setbit(key, offset, value));
    }

    public Boolean setbit(final String key, final long offset, final String value) {
        return execute(jedis -> jedis.setbit(key, offset, value));
    }

    public Boolean getbit(final String key, final long offset) {
        return execute(jedis -> jedis.getbit(key, offset));
    }

    public Long setrange(final String key, final long offset, final String value) {
        return execute(jedis -> jedis.setrange(key, offset, value));
    }

    public String getrange(final String key, final long startOffset, final long endOffset) {
        return execute(jedis -> jedis.getrange(key, startOffset, endOffset));
    }

    public String getSet(final String key, final String value) {
        return execute(jedis -> jedis.getSet(key, value));
    }

    public Long setnx(final String key, final String value) {
        return execute(jedis -> jedis.setnx(key, value));
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(jedis -> jedis.setex(key, seconds, value));
    }

    public List<String> blpop(final String arg) {
        return execute(jedis -> jedis.blpop(arg));
    }

    public List<String> brpop(final String arg) {
        return execute(jedis -> jedis.brpop(arg));
    }

    public Long decrBy(final String key, final long integer) {
        return execute(jedis -> jedis.decrBy(key, integer));
    }

    public Long decr(final String key) {
        return execute(jedis -> jedis.decr(key));
    }

    public Long incrBy(final String key, final long integer) {
        return execute(jedis -> jedis.incrBy(key, integer));
    }

    public Long incr(final String key) {
        return execute(jedis -> jedis.incr(key));
    }

    public Long append(final String key, final String value) {
        return execute(jedis -> jedis.append(key, value));
    }

    public String substr(final String key, final int start, final int end) {
        return execute(jedis -> jedis.substr(key, start, end));
    }

    public Long hset(final String key, final String field, final String value) {
        return execute(jedis -> jedis.hset(key, field, value));
    }

    public String hget(final String key, final String field) {
        return execute(jedis -> jedis.hget(key, field));
    }

    public Long hsetnx(final String key, final String field, final String value) {
        return execute(jedis -> jedis.hsetnx(key, field, value));
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return execute(jedis -> jedis.hmset(key, hash));
    }

    public List<String> hmget(final String key, final String... fields) {
        return execute(jedis -> jedis.hmget(key, fields));
    }

    public Long hincrBy(final String key, final String field, final long value) {
        return execute(jedis -> jedis.hincrBy(key, field, value));
    }

    public Boolean hexists(final String key, final String field) {
        return execute(jedis -> jedis.hexists(key, field));
    }

    public Long del(final String key) {
        return execute(jedis -> jedis.decr(key));
    }

    public Long hdel(final String key, final String... fields) {
        return execute(jedis -> jedis.hdel(key, fields));
    }

    public Long hlen(final String key) {
        return execute(jedis -> jedis.hlen(key));
    }

    public Set<String> hkeys(final String key) {
        return execute(jedis -> jedis.hkeys(key));
    }

    public List<String> hvals(final String key) {
        return execute(jedis -> jedis.hvals(key));
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(jedis -> jedis.hgetAll(key));
    }

    public Long rpush(final String key, final String... strings) {
        return execute(jedis -> jedis.rpush(key, strings));
    }

    public Long lpush(final String key, final String... strings) {
        return execute(jedis -> jedis.lpush(key, strings));
    }

    public Long lpushx(final String key, final String... string) {
        return execute(jedis -> jedis.lpushx(key, string));
    }

    public Long strlen(final String key) {
        return execute(jedis -> jedis.strlen(key));
    }

    public Long move(final String key, final int dbIndex) {
        return execute(jedis -> jedis.move(key, dbIndex));
    }


    public Long rpushx(final String key, final String... string) {
        return execute(jedis -> jedis.rpushx(key, string));
    }

    public Long persist(final String key) {
        return execute(jedis -> jedis.persist(key));
    }

    public Long llen(final String key) {
        return execute(jedis -> jedis.llen(key));
    }

    public List<String> lrange(final String key, final long start, final long end) {
        return execute(jedis -> jedis.lrange(key, start, end));
    }

    public String ltrim(final String key, final long start, final long end) {
        return execute(jedis -> jedis.ltrim(key, start, end));
    }

    public String lindex(final String key, final long index) {
        return execute(jedis -> jedis.lindex(key, index));
    }

    public String lset(final String key, final long index, final String value) {
        return execute(jedis -> jedis.lset(key, index, value));
    }

    public Long lrem(final String key, final long count, final String value) {
        return execute(jedis -> jedis.lrem(key, count, value));
    }

    public String lpop(final String key) {
        return execute(jedis -> jedis.lpop(key));
    }

    public String rpop(final String key) {
        return execute(jedis -> jedis.rpop(key));
    }

    public Long sadd(final String key, final String... members) {
        return execute(jedis -> jedis.sadd(key, members));
    }

    public Set<String> smembers(final String key) {
        return execute(jedis -> jedis.smembers(key));
    }

    public Long srem(final String key, final String... members) {
        return execute(jedis -> jedis.srem(key, members));
    }

    public String spop(final String key) {
        return execute(jedis -> jedis.spop(key));
    }

    public Long scard(final String key) {
        return execute(jedis -> jedis.scard(key));
    }

    public Boolean sismember(final String key, final String member) {
        return execute(jedis -> jedis.sismember(key, member));
    }

    public String srandmember(final String key) {
        return execute(jedis -> jedis.srandmember(key));
    }

    public Long zadd(final String key, final double score, final String member) {
        return execute(jedis -> jedis.zadd(key, score, member));
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return execute(jedis -> jedis.zadd(key, scoreMembers));
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        return execute(jedis -> jedis.zrange(key, start, end));
    }

    public Long zrem(final String key, final String... members) {
        return execute(jedis -> jedis.zrem(key, members));
    }

    public Double zincrby(final String key, final double score, final String member) {
        return execute(jedis -> jedis.zincrby(key, score, member));
    }

    public Long zrank(final String key, final String member) {
        return execute(jedis -> jedis.zrank(key, member));
    }

    public Long zrevrank(final String key, final String member) {
        return execute(jedis -> jedis.zrevrank(key, member));
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        return execute(jedis -> jedis.zrevrange(key, start, end));
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return execute(jedis -> jedis.zrangeWithScores(key, start, end));
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        return execute(jedis -> jedis.zrevrangeWithScores(key, start, end));
    }

    public Long zcard(final String key) {
        return execute(jedis -> jedis.zcard(key));
    }

    public Double zscore(final String key, final String member) {
        return execute(jedis -> jedis.zscore(key, member));
    }

    public List<String> sort(final String key) {
        return execute(jedis -> jedis.sort(key));
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        return execute(jedis -> jedis.sort(key, sortingParameters));
    }

    public Long zcount(final String key, final double min, final double max) {
        return execute(jedis -> jedis.zcount(key, min, max));
    }

    public Long zcount(final String key, final String min, final String max) {
        return execute(jedis -> jedis.zcount(key, min, max));
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return execute(jedis -> jedis.zrangeByScore(key, min, max));
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return execute(jedis -> jedis.zrevrangeByScore(key, max, min));

    }

    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return execute(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return execute(jedis -> jedis.zrevrangeByScore(key, min, max, offset, count));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return execute(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return execute(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return execute(jedis -> jedis.zrevrangeByScoreWithScores(key, min, max, offset, count));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return execute(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return execute(jedis -> jedis.zrangeByScore(key, min, max));
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        return execute(jedis -> jedis.zrevrangeByScore(key, max, min));
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return execute(jedis -> jedis.zrangeByScore(key, min, max, offset, count));
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return execute(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return execute(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return execute(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return execute(jedis -> jedis.zrangeByScoreWithScores(key, min, max, offset, count));
    }


    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min,
                                                 final int offset, final int count) {
        return execute(jedis -> jedis.zrevrangeByScoreWithScores(key, max, min, offset, count));
    }


    public Long zremrangeByRank(final String key, final long start, final long end) {
        return execute(jedis -> jedis.zremrangeByRank(key, start, end));
    }


    public Long zremrangeByScore(final String key, final double start, final double end) {
        return execute(jedis -> jedis.zremrangeByScore(key, start, end));
    }


    public Long zremrangeByScore(final String key, final String start, final String end) {
        return execute(jedis -> jedis.zremrangeByScore(key, start, end));
    }


    public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        return execute(jedis -> jedis.linsert(key, where, pivot, value));
    }

    public Long bitcount(final String key) {
        return execute(jedis -> jedis.bitcount(key));
    }

    public Long bitcount(final String key, final long start, final long end) {
        return execute(jedis -> jedis.bitcount(key, start, end));
    }

    public ScanResult<Entry<String, String>> hscan(final String key, final String cursor) {
        return execute(jedis -> jedis.hscan(key, cursor));
    }

    public ScanResult<String> sscan(final String key, final String cursor) {
        return execute(jedis -> jedis.sscan(key, cursor));
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return execute(jedis -> jedis.zscan(key, cursor));
    }

    public interface SyncLockCallback<T> {
        T callback();
    }

    private final static String SYNC_LOCK_SUFFIX = "_SYNC";

    /**
     * 同步保护
     */
    public <T> T sync(final String lock, final long expire, SyncLockCallback<T> callback) {
        if (callback == null) {
            throw new IllegalArgumentException();
        }
        if (acquire(lock + SYNC_LOCK_SUFFIX, expire)) {
            try {
                return callback.callback();
            } finally {
                release(lock + SYNC_LOCK_SUFFIX);
            }
        } else {
            return null;
        }
    }

    /**
     * 本地同步保护
     */
    public <T> T syncWithLock(Lock lock, long timeout, TimeUnit timeoutUnit,
                              SyncLockCallback<T> callback) {
        if (lock == null || timeoutUnit == null || callback == null) {
            throw new IllegalArgumentException();
        }
        try {
            if (lock.tryLock(timeout, timeoutUnit)) {
                try {
                    return callback.callback();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 通过SETNX试图获取一个锁
     *
     * @param expire 存活时间(秒)
     */
    private boolean acquire(final String key, final long expire) {
        return execute(jedis -> {
            boolean success = false;
            try {
                long value = System.currentTimeMillis() + expire * 1000 + 1;
                // 通过setnx获取一个lock
                Long acquired = jedis.setnx(key, String.valueOf(value));
                // setnx成功，则成功获取一个锁
                if (acquired != null && acquired > 0) {
                    if (expire > 0) {
                        try {
                            jedis.expire(key, (int) expire);
                        } catch (Throwable e) {
                            logger.error("", e);
                        }
                    }
                    success = true;
                }
                // setnx失败，说明锁仍然被其他对象保持，检查其是否已经超时
                else {
                    // 当前锁过期时间
                    String v = jedis.get(key);
                    if (StringUtils.isNotBlank(v)) {
                        long oldValue = Long.valueOf(v);
                        // 超时
                        if (oldValue < System.currentTimeMillis()) {
                            // 查看是否有并发
                            String oldValueAgain = jedis.getSet(key, String.valueOf(value));
                            // 获取锁成功
                            if (Long.valueOf(oldValueAgain) == oldValue) {
                                if (expire > 0) {
                                    try {
                                        jedis.expire(key, (int) expire);
                                    } catch (Throwable e) {
                                        logger.error("", e);
                                    }
                                }
                                success = true;
                            }
                            // 已被其他进程捷足先登了
                            else {
                                success = false;
                            }
                        } else {
                            // 未超时，则直接返回失败
                            success = false;
                        }
                    }
                }
            } catch (Throwable e) {
                logger.error("", e);
            }
            return success;
        });
    }

    private final static Pattern LCK_TIME = Pattern.compile("\\d+");

    /**
     * 释放锁
     */
    private void release(final String key) {
        execute(jedis -> {
            try {
                String lckUUID = jedis.get(key);
                if (lckUUID == null || !LCK_TIME.matcher(lckUUID).find()) {
                    return null;
                }
                Long getValue = Long.parseLong(lckUUID);
                // 避免删除非自己获取得到的锁
                if (System.currentTimeMillis() < getValue.longValue()) {
                    jedis.del(key);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            return null;
        });
    }

    private byte[] serialize(Object object) {
        return getSerializer().serialize(object);
    }

    private <T> T deserialize(byte[] byteArray, Class<T> c) {
        return getSerializer().deserialize(byteArray, c);
    }

    private <E> List<E> deserializeForList(byte[] byteArray, Class<E> elementC) {
        return getSerializer().deserializeForList(byteArray, elementC);
    }

    public RedisSerializer getSerializer() {
        if (this.serializer == null) {
            synchronized (this) {
                if (this.serializer == null) {
                    serializer = new JsonSerializer();
                }
            }
        }
        return serializer;
    }

    public void setSerializer(RedisSerializer serializer) {
        this.serializer = serializer;
    }
}
