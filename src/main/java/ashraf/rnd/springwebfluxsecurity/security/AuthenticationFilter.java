package ashraf.rnd.springwebfluxsecurity.security;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.repository.TokenRedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFilter implements WebFilter {
    private final TokenRedisRepository tokenRedisRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("AuthenticationFilter:: filter:: reached {}",System.currentTimeMillis());
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (AuthenticationTokenData) securityContext.getAuthentication().getPrincipal())
                .defaultIfEmpty(AuthenticationTokenData.builder().build())
//                .filter(authenticationTokenData -> authenticationTokenData.getUserAudience() != null)
//                .flatMap(token -> chain.filter(decorate(exchange, token)))
//                .switchIfEmpty(chain.filter(exchange));
                .flatMap(token -> {
                    if (token.getUserAudience() != null) {
                        return chain.filter(decorate(exchange, token));
                    }
                    return chain.filter(exchange);
                });


    }

    private ServerWebExchange decorate(ServerWebExchange exchange, AuthenticationTokenData token) {
        final ServerHttpRequest decorated = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().collectList()
                        .flatMapMany(dataBuffers -> authenticateToken(token).flatMapIterable(aBoolean -> dataBuffers));
            }
        };

        return new ServerWebExchangeDecorator(exchange) {
            @Override
            public ServerHttpRequest getRequest() {
                return decorated;
            }
        };
    }

    private Mono<?> authenticateToken(AuthenticationTokenData appUser) {
        return verifyAppUser(appUser)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new RuntimeException("INVALID_USER_TOKEN")));
    }


    private Mono<Boolean> verifyAppUser(AuthenticationTokenData appUser) {

        return getAppUser(appUser.getUserAudience())
                .map(appUserCacheData -> verifyUserData(appUserCacheData, appUser))
                .defaultIfEmpty(Boolean.FALSE);
    }

    private boolean verifyUserData(AuthenticationTokenData userFromCache, AuthenticationTokenData userFromToken) {

        return userFromCache.getUserAudience().equals(userFromToken.getUserAudience()) &&
                userFromCache.getUserId().equals(userFromToken.getUserId()) &&
                userFromCache.getDeviceId().equals(userFromToken.getDeviceId()) &&
                userFromCache.getToken().equals(userFromToken.getToken());
    }

    public Mono<AuthenticationTokenData> getAppUser(String userAudience) {
        return tokenRedisRepository.getAppUserData(userAudience);
    }


}
