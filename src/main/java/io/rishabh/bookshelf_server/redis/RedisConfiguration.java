package io.rishabh.bookshelf_server.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Value("${cache.redis.host}")
    private String REDIS_HOST;
    @Value("${cache.redis.port}")
    private int REDIS_PORT;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(REDIS_HOST);
        standaloneConfiguration.setPort(REDIS_PORT);
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(standaloneConfiguration);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        // add 10 mins expiration
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));
    }
}
