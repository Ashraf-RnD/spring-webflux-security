package ashraf.rnd.springwebfluxsecurity.security;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
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

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (AppUser) securityContext.getAuthentication().getPrincipal())
                .defaultIfEmpty(AppUser.builder().build())
//                .filter(authenticationTokenData -> authenticationTokenData !=null)
//                .flatMap(appUser -> chain.filter(decorate(exchange, appUser)))
//                .switchIfEmpty(chain.filter(exchange));

                .flatMap(appUser -> {
                    if (appUser != null) {
                        return chain.filter(decorate(exchange, appUser));
                    }
                    return chain.filter(exchange);
                });


    }

    private ServerWebExchange decorate(ServerWebExchange exchange, AppUser appUser) {
        final ServerHttpRequest decorated = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().collectList()
                        .flatMapMany(dataBuffers -> authenticateToken(appUser).flatMapIterable(aBoolean -> dataBuffers));
            }
        };

        return new ServerWebExchangeDecorator(exchange) {
            @Override
            public ServerHttpRequest getRequest() {
                return decorated;
            }
        };
    }

    private Mono<?> authenticateToken(AppUser appUser) {
        return verifyAppUser(appUser)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new RuntimeException("INVALID_USER_TOKEN")));
    }


    private Mono<Boolean> verifyAppUser(AppUser appUser) {

        return getAppUser(appUser.getUserAudience())
                .map(userFromCache -> verifyUserData(appUser, userFromCache))
                .defaultIfEmpty(false);
    }

    private boolean verifyUserData(AppUser appUser, AuthenticationTokenData userFromCache) {

        return userFromCache.getUserAudience().equals(appUser.getUserAudience()) &&
                userFromCache.getUserId().equals(appUser.getUserId()) &&
                userFromCache.getDeviceId().equals(appUser.getDeviceId());
//        &&
//                userFromCache.getToken().equals(appUser.getToken());
    }

    public Mono<AuthenticationTokenData> getAppUser(String userIdentityNumber) {

        return tokenRedisRepository.getAppUserData(userIdentityNumber);
    }


}
