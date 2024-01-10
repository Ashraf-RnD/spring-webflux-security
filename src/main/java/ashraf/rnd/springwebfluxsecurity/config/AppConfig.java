package ashraf.rnd.springwebfluxsecurity.config;

import ashraf.rnd.springwebfluxsecurity.utility.JwtAlgorithmManager;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.validation.Validator;

import static ashraf.rnd.springwebfluxsecurity.utility.EncryptionUtility.PRIVATE_KEY;
import static ashraf.rnd.springwebfluxsecurity.utility.EncryptionUtility.PUBLIC_KEY;


@Configuration
public class AppConfig {

    @Bean
    public Algorithm getTokenAlgorithm() {
        var helper = new JwtAlgorithmManager(PUBLIC_KEY, PRIVATE_KEY);
        return helper.getTokenAlgorithm();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
