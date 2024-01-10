
## JWT
### Step 1
```java
class RsaUtil{
    //Load RSA certificates
    PrivateKey getPrivateKey(String privateKeyStr);
    PublicKey getPublicKey(String publicKeyStr);

    //Crypto
    String encrypt(String plainText, String publicKeyStr);
    String decrypt(String encryptedText, String privateKeyStr);
}
```
### Step 2
**Password protected RSA**
```shell
openssl genrsa -des3 -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
openssl rsa -outform der -in private.pem -out private.key
mv public.pem public.key
```
**Non Password RSA**

```shell
openssl genrsa -out privatekey.pem 2048
openssl rsa -in privatekey.pem -pubout -out publickey.pem
```

### Step 3
```java
class JwtAlgorithmManager {
    
}
```

### Step 4
```java
class JwtService {
    
}
```

## Spring webflux security
### Step 1
**Authentication Manage** : 
```java
class AuthenticationManager implements ReactiveAuthenticationManager{
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return null;
    }
}
```

### Step 2
Security Context Repository : 
```java
class SecurityContextRepository implements ServerSecurityContextRepository{
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return null;
    }
}
```

### Step 3
Security Config : 
```java
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {
    
}
```

### Step 4
Authentication Filter : 
```java
public class AuthenticationFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return null;
    }
}
```