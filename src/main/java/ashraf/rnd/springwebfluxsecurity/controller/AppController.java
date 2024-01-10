package ashraf.rnd.springwebfluxsecurity.controller;

import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import ashraf.rnd.springwebfluxsecurity.model.response.TokenResponse;
import ashraf.rnd.springwebfluxsecurity.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    @PostMapping("/token/get")
    public Mono<TokenResponse> getAuthToken(@RequestBody AppUser request, @RequestHeader HttpHeaders httpHeaders) {

        log.info("AppController:: getAuthToken:: request: {}",request);
        return appService.getAuthToken(request);
    }
}
