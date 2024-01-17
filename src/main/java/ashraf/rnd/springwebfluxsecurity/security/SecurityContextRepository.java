package ashraf.rnd.springwebfluxsecurity.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;


    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {

        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        log.info("SecurityContextRepository:: load:: reached {}",System.currentTimeMillis());

        return Mono.just(serverWebExchange.getRequest())
                .mapNotNull(serverHttpRequest -> serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authenticationHeader -> authenticationHeader !=null && authenticationHeader.startsWith(TOKEN_PREFIX))
                .switchIfEmpty(Mono.empty())
                .map(authHeader -> authHeader.replace(TOKEN_PREFIX,"".trim()))
                .flatMap(authToken -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authToken, authToken)))
                .map(SecurityContextImpl::new);
    }
}
