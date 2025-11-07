package com.sicheng.langchain4j03bootintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置 Redis 连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key 使用 String 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value 使用 JSON 序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash 的 key 使用 String 序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash 的 value 使用 JSON 序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 确保配置生效
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
