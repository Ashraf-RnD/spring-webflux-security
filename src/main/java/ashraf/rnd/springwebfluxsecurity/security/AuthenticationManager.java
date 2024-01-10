package ashraf.rnd.springwebfluxsecurity.security;


import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.service.JwtService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static ashraf.rnd.springwebfluxsecurity.utility.JwtUtility.TOKEN_CLAIM_DEVICE;
import static ashraf.rnd.springwebfluxsecurity.utility.JwtUtility.TOKEN_CLAIM_USER;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final JwtService tokenService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("AuthenticationManager:: authenticate:: reached {}", System.currentTimeMillis());
        return Mono.just(authentication.getCredentials().toString())
                .flatMap(this::verifyToken)
                .flatMap(this::getAuthentication);
    }

    private Mono<Authentication> getAuthentication(AuthenticationTokenData appUser) {
        return Mono.just(
                new UsernamePasswordAuthenticationToken(appUser, null,
                        getGrantedAuthorities())
        );
    }

    private Collection<SimpleGrantedAuthority> getGrantedAuthorities() {

        return List.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    private Mono<AuthenticationTokenData> verifyToken(String token) {

        return tokenService.verifyToken(token)
                .filter(decodedJWT -> decodedJWT.getAudience() != null && !decodedJWT.getAudience().isEmpty())
                .flatMap(tokenData -> getAppUser(tokenData, token));
    }

    private Mono<AuthenticationTokenData> getAppUser(DecodedJWT decodedJWT, String token) {
        return Mono.just(AuthenticationTokenData.builder()
                .userAudience(decodedJWT.getAudience().get(0))
                .userId(decodedJWT.getClaim(TOKEN_CLAIM_USER).asString())
                .deviceId(decodedJWT.getClaim(TOKEN_CLAIM_DEVICE).asString())
                .token(token)
                .build()
        );
    }
}
