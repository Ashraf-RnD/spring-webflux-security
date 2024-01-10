package ashraf.rnd.springwebfluxsecurity.repository;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.utility.MapperUtils;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static ashraf.rnd.springwebfluxsecurity.utility.JwtUtility.TOKEN_ISSUER;

@Repository
public class TokenRedisRepositoryImpl extends AbstractRedisRepository implements TokenRedisRepository{
    public TokenRedisRepositoryImpl(ReactiveRedisTemplate<String, String> redisTemplate, MapperUtils mapperUtils) {
        super(redisTemplate, mapperUtils);
    }

    @Override
    public Mono<Boolean> saveToken(AuthenticationTokenData authenticationTokenData, long keyExpiresInSecond) {
        return Mono.just(TOKEN_ISSUER.concat("-").concat(authenticationTokenData.getUserAudience()))
                .flatMap(hashKey -> saveData(hashKey, authenticationTokenData.getUserAudience(), authenticationTokenData)
                        .flatMap(isSaved -> setExpiry(hashKey, keyExpiresInSecond)));
    }

    @Override
    public Mono<AuthenticationTokenData> getAppUserData(String audience) {
        return null;
    }
}
