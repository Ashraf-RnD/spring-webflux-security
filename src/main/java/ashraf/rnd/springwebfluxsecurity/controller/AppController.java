package ashraf.rnd.springwebfluxsecurity.controller;

import ashraf.rnd.springwebfluxsecurity.model.request.AppUser;
import ashraf.rnd.springwebfluxsecurity.model.request.OperationRequest;
import ashraf.rnd.springwebfluxsecurity.model.response.OperationResponse;
import ashraf.rnd.springwebfluxsecurity.model.response.TokenResponse;
import ashraf.rnd.springwebfluxsecurity.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

    @PostMapping("/token/get")
    public Mono<TokenResponse> getAuthToken(@RequestBody AppUser request) {
        return appService.getAuthToken(request);
    }

    @PostMapping("/operation/create")
    public Mono<OperationResponse> createOperation(@RequestBody OperationRequest request) {
        return Mono.just(OperationResponse.builder().opReqId(request.getOpReqId()).boTime(new Date().toString()).build());
    }

}
