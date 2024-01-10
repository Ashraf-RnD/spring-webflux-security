package ashraf.rnd.springwebfluxsecurity.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;


    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {

        return DefaultClientResources.create();
    }

    @Bean
    public ClientOptions clientOptions() {

        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build();
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(ClientResources clientResources, ClientOptions clientOptions) {
        var clientConfig = LettuceClientConfiguration.builder()
                .clientResources(clientResources)
                .clientOptions(clientOptions)
                .build();

        var serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        return new LettuceConnectionFactory(serverConfig, clientConfig);

    }

}
