package ashraf.rnd.springwebfluxsecurity.service;

import ashraf.rnd.springwebfluxsecurity.model.entity.redis.AuthenticationTokenData;
import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import ashraf.rnd.springwebfluxsecurity.model.response.TokenResponse;
import ashraf.rnd.springwebfluxsecurity.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static ashraf.rnd.springwebfluxsecurity.utility.JwtUtility.getTokenExpirationTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {

    private final JwtService tokenService;
    private final TokenRedisRepository tokenRedisRepository;

    public Mono<TokenResponse> getAuthToken(AppUser request) {
        log.info("AppService:: getAuthToken:: request: {}", request);
        return tokenService.generateToken(request, getTokenExpirationTime())
                .flatMap(accessToken -> getSaveToken(request, accessToken))
                .map(token -> TokenResponse.builder().accessToken(token).expiresInSecond(300L).build());

    }

    private Mono<String> getSaveToken(AppUser request, String accessToken) {
        log.info("AppService:: getAuthToken:: request: {}, token: {}", request, accessToken);
        return tokenRedisRepository.saveToken(AuthenticationTokenData.builder()
                        .userAudience(request.getUserAudience())
                        .token(accessToken)
                        .userId(request.getUserId())
                        .deviceId(request.getDeviceId())
                        .build(), 300)
                .flatMap(saved -> Mono.just(accessToken));
    }

}

