/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.component;

import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.common.exception.UtilityException;
import com.bubble.pilipili.common.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 公共Redis操作类
 * @author Bubble
 * @date 2025.03.26 17:05
 */
@Slf4j
//@Component // 抽象类不能直接注入，需要实现子类后注入子类
public abstract class RedisHelper {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected RedissonClient redissonClient;

    /**
     * 分布式锁获取等待时间（秒）
     */
    protected static final Integer RLOCK_WAIT_TIME = 10;
    /**
     * 分布式锁获取持有超时时间（秒）
     */
    protected static final Integer RLOCK_LEASE_TIME = 10;

    private static final Random RANDOM = new Random();
    /**
     * 缓存过期基础时间：3天，为了防止缓存集中过期，设置过期时间时应取浮动值
     */
    public static final long EXPIRE_TIME = 60 * 60 * 24 * 3;
    /**
     * 缓存过期时间浮动范围：上下2小时
     */
    public static final long EXPIRE_TIME_VARIANT = 60 * 60 * 2;

    /**
     * 空值缓存时间：10分钟
     */
    public static final long NULL_VALUE_EXPIRE_TIME = 60 * 10;


    /**
     * 执行经过缓存的查询。先查询缓存，若缓存未命中则查询数据库
     * @param param 目标参数（通常是ID）
     * @param cacheRootKey 缓存key前缀
     * @param repoGetter 持久层查询函数
     * @return 目标结果
     * @param <T> 目标参数类型（Integer或String）
     * @param <R> 结果对象类型
     */
    public <T, R> R queryViaCache(
            T param,
            RedisKey cacheRootKey,
            Function<T, R> repoGetter,
            Class<R> resultClz
    ) {
        if (param == null) {
            return null;
        }
        String cacheKey = getCacheKey(cacheRootKey, param);
        Object cache = getCache(cacheKey);

        // 缓存未命中则查询数据库
        if (cache == null) {
            R repoRecord = repoGetter.apply(param);
            runRLockTask(
                    getRLockName(cacheRootKey.getKey(), param.toString()),
                    () -> saveCache(cacheKey, repoRecord),
                    cacheKey
            );
            return repoRecord;
        }
        if (resultClz.isInstance(cache)) {
            return resultClz.cast(cache);
        } else {
            // 缓存命中空值
            return null;
        }
    }


    /**
     * 支持两个参数的执行经过缓存的查询。先查询缓存，若缓存未命中则查询数据库
     * <pre>
     * 关于Function<T, R>的使用，注意以下事项：
     *  1.   当传入静态方法引用时 -- VideoInfo::getVid，
     *       第一个泛型参数是实际调用该方法的实例对象，例如func.apply(video)，这里就相当于video.getVid()。
     *  2.   当传入实例方法引用时 -- videoRedisHelper::getVideoInfo，
     *       第一个泛型参数是传入方法的第一个参数，例如func.apply(vid)，相当于videoRedisHelper.getVideoInfo(vid)。
     *  3.   BiFunction<T, U, R>则是扩展了一个参数，对于静态方法则可以传入一个方法参数（U）
     *       对于实例方法则可以传入两个方法参数（T、U）
     *  4.   本Redis缓存操作工具类中，针对Function函数接口参数类型的方法，传入的基本都是实例方法，例如上面第二点提到的；
     *       在MyBatisPlus的LambdaQueryWrapper中更多的用到静态方法（例如VideoInfo::getVid），
     *       实际上这里的getVid并不是静态方法，但MyBatisPlus支持这么写
     * </pre>
     * @param param 目标参数1（通常是ID）
     * @param param2 目标参数2（通常是ID）
     * @param cacheRootKey 缓存key前缀
     * @param repoGetter 持久层查询函数
     * @return 目标结果
     * @param <T> 目标参数类型（Integer或String）
     * @param <R> 结果对象类型
     */
    public <T, R> R queryViaCache2(
            T param,
            T param2,
            RedisKey cacheRootKey,
            BiFunction<T, T, R> repoGetter,
            Class<R> resultClz
    ) {
        String cacheKey = getCacheKey(cacheRootKey, param, param2);
        Object cache = getCache(cacheKey);
        // 缓存未命中则查询数据库
        if (cache == null) {
            R repoRecord = repoGetter.apply(param, param2);
            runRLockTask(
                    getRLockName(cacheRootKey.getKey(), param.toString(), param2.toString()),
                    () -> saveCache(cacheKey, repoRecord),
                    cacheKey
            );
            return repoRecord;
        }
        if (resultClz.isInstance(cache)) {
            return resultClz.cast(cache);
        } else {
            // 缓存命中空值
            return null;
        }
    }


    protected String getCacheKey(RedisKey cacheRootKey, Object... param) {
        if (param == null || param.length == 0) {
            throw new UtilityException("param is null or empty");
        }
        String cacheKey;
        if (param[0] instanceof String) {
            cacheKey = concatKey(cacheRootKey.getKey(), Arrays.copyOf(param, param.length, String[].class));
        } else if (param[0] instanceof Integer) {
            cacheKey = concatKey(cacheRootKey.getKey(), Arrays.copyOf(param, param.length, Integer[].class));
        } else {
            cacheKey = null;
            log.warn("param [{}] is not a String nor an Integer", param);
        }
        return cacheKey;
    }

    /**
     * 保存视频任务ID映射（1天后过期）
     * @param taskId 视频上传任务ID
     * @param vid 视频信息主键ID
     */
    public void saveVideoTask(String taskId, Integer vid) {
        String key = getVideoTaskMapKey(taskId);
        if (vid == null) {
            redisTemplate.opsForValue().set(key, new NullValue(), 1, TimeUnit.DAYS);
            log.info("Save VideoTask map: {}:[NullValue]", key);
        } else {
            redisTemplate.opsForValue().set(key, vid, 1, TimeUnit.DAYS);
            log.info("Save VideoTask map: {}:{}", key, vid);
        }
    }

    public Integer getVideoTaskVid(String taskId) {
        Object cache = redisTemplate.opsForValue().get(getVideoTaskMapKey(taskId));
        if (cache == null || cache instanceof NullValue) {
            return null;
        }
        return (Integer) cache;
    }

    /**
     * 检查视频上传任务是否已完成<br>
     * 若该taskId缓存值为{@link NullValue}，则表示OSS已完成视频上传，若为空则表示上传还未完成
     * @param taskId OSS视频上传任务ID
     * @return
     */
    public boolean isVideoTaskUploadSuccess(String taskId) {
        Object cache = redisTemplate.opsForValue().get(getVideoTaskMapKey(taskId));
        if (cache == null) {
            return false;
        }
        return cache instanceof NullValue;
    }

    /**
     * 删除视频上传任务缓存值
     * @param taskId
     */
    public void removeVideoTask(String taskId) {
        redisTemplate.delete(getVideoTaskMapKey(taskId));
    }


    /**
     * 拼接key
     * @param root
     * @param ids
     * @return
     */
    protected String concatKey(String root, Integer... ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(root);
        for (int id : ids) {
            sb.append(RedisKey.KEY_DIVIDER.getKey()).append(id);
        }
        return sb.toString();
    }
    /**
     * 拼接key
     * @param root
     * @param uid
     * @param secRoot
     * @param ids
     * @return
     */
    protected String concatKey(String root, Integer uid, String secRoot, Integer... ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(root)
                .append(RedisKey.KEY_DIVIDER.getKey()).append(uid)
                .append(RedisKey.KEY_DIVIDER.getKey()).append(secRoot);
        for (int id : ids) {
            sb.append(RedisKey.KEY_DIVIDER.getKey()).append(id);
        }
        return sb.toString();
    }
    /**
     * 拼接key
     * @param root
     * @param ids
     * @return
     */
    protected String concatKey(String root, String... ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(root);
        for (String id : ids) {
            sb.append(RedisKey.KEY_DIVIDER.getKey()).append(id);
        }
        return sb.toString();
    }

    protected <V> void saveCache(String key, V value) {
        long timeout = ((long) (EXPIRE_TIME + ((RANDOM.nextBoolean()? 1:-1) * RANDOM.nextDouble() * EXPIRE_TIME_VARIANT)));
        saveCache(key, value, timeout);
    }
    protected <V> void saveCache(String key, V value, long timeout) {
        // value为null，即查询数据库结果为空，缓存空串（因为redisTemplate会将存储null值视为不存储任何值，无法起到缓存空值的作用，要手动存储空串）
        // 这里使用NullValue代指空值
        if (value == null) {
            redisTemplate.opsForValue().set(
                    key, new NullValue(),
                    NULL_VALUE_EXPIRE_TIME,
                    TimeUnit.SECONDS
            );
        } else {
            redisTemplate.opsForValue().set(
                    key, value, timeout, TimeUnit.SECONDS
            );
        }
    }

    public void removeCache(RedisKey cacheRootKey, Integer... ids) {
        redisTemplate.delete(concatKey(cacheRootKey.getKey(), ids));
    }

    public void removeCacheByKeyPattern(RedisKey cacheRootKey, Integer... ids) {
        String pattern = concatKey(cacheRootKey.getKey(), ids) + "*";
        // keys方法时阻塞的，慎用。
//        Set<String> keys = redisTemplate.keys(pattern);

        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();
        Set<String> keys = new HashSet<>();
        try (Cursor<String> scan = redisTemplate.scan(scanOptions)) {
            while (scan.hasNext()) {
                keys.add(scan.next());
            }
        }
        redisTemplate.delete(keys);
    }

    protected Object getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 带锁更新视频任务缓存
     * @param taskId 视频任务ID，作为缓存键
     * @param vid 视频信息主键ID，作为缓存值，支持缓存空值
     * @param fallback 双重检查发现缓存已被更新，备用任务
     */
    public void saveVideoTaskIdWithRLockTask(String taskId, Integer vid, Consumer<Object> fallback) {
        String lockName = getRLockName(RedisKey.VIDEO_TASK_MAP.getKey(), taskId);
        String key = getVideoTaskMapKey(taskId);

        Runnable task = () -> saveVideoTask(taskId, vid);
//        Consumer<Object> innerFallback = (cache) -> {
////            if (cache instanceof NullValue) {
////                return;
////            }
////            Integer vidCache = (Integer) cache;
//            fallback.accept(cache);
//        };
        runRLockTask(lockName, task, fallback, key);
    }

    /**
     * 执行分布式锁任务
     * @param lockName 分布式锁名称
     * @param task 任务
     * @param key 更新缓存key，用于双重检查
     */
    protected void runRLockTask(String lockName, Runnable task, String key) {
        runRLockTask(lockName, task, null, key);
    }
    /**
     * 执行分布式锁任务，带双重检查
     * @param lockName 分布式锁名称
     * @param task 任务
     * @param fallback 缓存已被更新，备用任务
     * @param key 更新缓存key，用于双重检查
     */
    protected void runRLockTask(String lockName, Runnable task, Consumer<Object> fallback, String key) {
        RLock lock = redissonClient.getLock(lockName);
        log.debug("RLock tryLock: {}", lockName);
        try {
            if (lock.tryLock(RLOCK_WAIT_TIME, RLOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                log.debug("RLock tryLock success: {}", lockName);
                try {
                    // Double Check 双重检查，确保在第一次检查缓存 到此处成功获取到锁 的期间，没有其他线程成功更新缓存，否则放弃更新缓存
                    Object cache = getCache(key);
                    if (cache == null) {
                        task.run();
                    } else if (fallback != null) {
                        fallback.accept(cache);
                    }
                } catch (Exception e) {
                  log.error("RLock [{}] task or fallback run error", lockName, e);
                } finally {
                    lock.unlock();
                }
            } else {
                log.error("RLock tryLock failed: {}", lockName);
            }
        } catch (InterruptedException e) {
            log.warn("Redisson lock [{}] interrupted", lock.getName());
            Thread.currentThread().interrupt();
        }
    }

    protected <S> String getRLockName(Class<S> clz, Integer... ids) {
        return getRLockName(clz, Arrays.toString(ids));
    }
    protected <S> String getRLockName(Class<S> clz, String... ids) {
        if (clz.equals(VideoStats.class)) {
            return getRLockName(RedisKey.VIDEO_STATS.getKey(), ids);
        } else if (clz.equals(DynamicStats.class)) {
            return getRLockName(RedisKey.DYNAMIC_STATS.getKey(), ids);
        } else if (clz.equals(CommentStats.class)) {
            return getRLockName(RedisKey.COMMENT_STATS.getKey(), ids);
        } else if (clz.equals(DanmakuStats.class)) {
            return getRLockName(RedisKey.DANMAKU_STATS.getKey(), ids);
        }
        return getRLockName(clz.getSimpleName(), ids);
    }
    protected String getRLockName(String rootKey, String... ids) {
        return "RLock" + RedisKey.KEY_DIVIDER.getKey() + concatKey(rootKey, ids);
    }


    private String getVideoTaskMapKey(String taskId) {
        return RedisKey.VIDEO_TASK_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + taskId;
    }


}
