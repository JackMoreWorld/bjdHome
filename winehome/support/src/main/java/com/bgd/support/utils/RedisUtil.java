package com.bgd.support.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, String value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        String result = null;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String getNull2Zero(final String key) {
        String result = null;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result == null ? "0" : result;
    }

    /**
     * 从redis取数据
     *
     * @param key
     * @return
     */
    public Long getRedisLong(String key) {
        return Long.valueOf(this.get(key) == null ? "0" : this.get(key));
    }


    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @描述 自增
     * @创建人 JackMore
     * @创建时间 2019/3/9
     */
    public long incr(String key, long step) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Long result = operations.increment(key, step);
        return result;
    }


//*******************************有序集合操作************************

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public boolean zAdd(String key, String value, long score) {
        boolean result = false;
        try {
            ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
            ZSetOperations.add(key, value, score);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 有序集合size
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        return ZSetOperations.zCard(key);
    }

    /**
     * 有序集合获取列表
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();

        return ZSetOperations.range(key, start, end);
    }


    /**
     * 移除指定元素
     *
     * @param key
     * @param value
     * @return
     */
    public Long zRem(String key, String value) {
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        return ZSetOperations.remove(key, value);
    }

}