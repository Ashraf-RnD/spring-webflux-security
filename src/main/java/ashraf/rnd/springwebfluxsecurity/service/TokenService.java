package ashraf.rnd.springwebfluxsecurity.service;

import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface TokenService {
    Mono<String> generateToken(AppUser request, Date tokenExpirationTime);

    Mono<DecodedJWT> verifyToken(String token);
}
