package ashraf.rnd.springwebfluxsecurity.repository;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import reactor.core.publisher.Mono;

public interface TokenRedisRepository {

    Mono<Boolean> saveToken(AuthenticationTokenData authenticationTokenData, long keyExpiresInSecond);
    Mono<AuthenticationTokenData> getAppUserData(String audience);
}
