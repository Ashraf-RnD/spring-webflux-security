package ashraf.rnd.springwebfluxsecurity.repository;

import ashraf.rnd.springwebfluxsecurity.utility.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

@AllArgsConstructor
public abstract class AbstractRedisRepository {

    private static final String REDIS_HASH_KEY_PREFIX = "app";
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    protected final MapperUtils mapperUtils;

    private String getRedisHashKey(String hashKey) {

        return REDIS_HASH_KEY_PREFIX + hashKey;
    }

    protected <T> Mono<T> getData(String hashKey, Object dataKey, Class<T> tClass) {

        return redisTemplate.opsForHash().get(getRedisHashKey(hashKey), dataKey)
                .map(o -> mapperUtils.deserialize(o, tClass));
    }

    protected Mono<Boolean> saveData(String hashKey, Object dataKey, Object data) {

        return redisTemplate.opsForHash().put(getRedisHashKey(hashKey), dataKey, mapperUtils.serialize(data));
    }

    protected Mono<Boolean> setExpiry(String hashKey, long timeoutInSecond) {

        return redisTemplate.expire(getRedisHashKey(hashKey), Duration.ofSeconds(timeoutInSecond));
    }
}
