package ashraf.rnd.springwebfluxsecurity.service;

import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

import static ashraf.rnd.springwebfluxsecurity.utility.JwtUtility.*;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService implements TokenService {

    private final Algorithm algorithm;


    @Override
    public Mono<String> generateToken(AppUser request, Date tokenExpirationTime) {
        return Mono.just(JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withAudience(request.getUserAudience())
                .withArrayClaim(TOKEN_SCOPE, TOKEN_SCOPE_ARRAY)
                .withClaim(TOKEN_CLAIM_USER, request.getUserId())
                .withClaim(TOKEN_CLAIM_DEVICE, request.getDeviceId())
                .withExpiresAt(tokenExpirationTime)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withSubject(TOKEN_SUBJECT)
                .sign(algorithm));
    }

    @Override
    public Mono<DecodedJWT> verifyToken(String token) {
        try {
            return Mono.just(JWT.require(algorithm)
                            .withIssuer(TOKEN_ISSUER)
                            .build())
                    .map(verifier -> verifier.verify(token));
        } catch (SignatureVerificationException
                 | AlgorithmMismatchException
                 | TokenExpiredException
                 | InvalidClaimException e) {
            return Mono.error(() -> new RuntimeException("Token Verification Failed - {}", e));
        }
    }


}
