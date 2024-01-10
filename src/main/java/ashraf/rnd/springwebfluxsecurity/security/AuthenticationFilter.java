package ashraf.rnd.springwebfluxsecurity.security;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import ashraf.rnd.springwebfluxsecurity.repository.TokenRedisRepository;
import ashraf.rnd.springwebfluxsecurity.service.JwtService;
import ashraf.rnd.springwebfluxsecurity.service.TokenService;
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

import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFilter implements WebFilter {
    private final TokenRedisRepository tokenRedisRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (String) securityContext.getAuthentication().getPrincipal())
                .defaultIfEmpty("")
//                .filter(Objects::nonNull)
//                .flatMap(token -> chain.filter(decorate(exchange, token)))
//                .switchIfEmpty(chain.filter(exchange));

                .flatMap(token -> {
                    if (token != null) {
                        return chain.filter(decorate(exchange, token));
                    }
                    return chain.filter(exchange);
                });


    }

    private ServerWebExchange decorate(ServerWebExchange exchange, String token) {
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

    private Mono<?> authenticateToken(String token) {
        return verifyAppUser(token)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new RuntimeException("INVALID_USER_TOKEN")));
    }


    private Mono<Boolean> verifyAppUser(String token) {

        return Mono.just(true);
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
