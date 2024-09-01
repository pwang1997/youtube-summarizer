package com.pwang.youtubesummarizer.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Puck Wang
 * @project youtubeSummarizer
 * @created 8/28/2024
 */


@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Slf4j
public class JedisConfig {

  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.password}")
  private String redisPassword;


  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    log.info("Loading JedisConnectionFactory on hostname {}", redisHost);
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    jedisPoolConfig.setMaxTotal(100);
    jedisPoolConfig.setMaxIdle(100);
    jedisPoolConfig.setMinIdle(10);
    jedisPoolConfig.setMaxWait(Duration.ofMillis(1000L));

    JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
        .connectTimeout(Duration.ofMillis(1000L))
        .readTimeout(Duration.ofMillis(1000L))
        .usePooling().poolConfig(jedisPoolConfig)
        .build();

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
        redisHost);
    redisStandaloneConfiguration.setPassword(redisPassword);
    return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    return redisTemplate;
  }
}
