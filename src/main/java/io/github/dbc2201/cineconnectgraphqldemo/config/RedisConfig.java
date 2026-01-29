package io.github.dbc2201.cineconnectgraphqldemo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis configuration for caching.
 * Provides caching for frequently accessed data like movies, users, and statistics.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    public static final String CACHE_MOVIES = "movies";
    public static final String CACHE_USERS = "users";
    public static final String CACHE_GENRES = "genres";
    public static final String CACHE_MOODS = "moods";
    public static final String CACHE_STATS = "stats";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            )
            .disableCachingNullValues();

        // Custom TTLs for different cache types
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Movies cache - longer TTL since movies don't change often
        cacheConfigurations.put(CACHE_MOVIES,
            defaultConfig.entryTtl(Duration.ofHours(1)));

        // Users cache - moderate TTL
        cacheConfigurations.put(CACHE_USERS,
            defaultConfig.entryTtl(Duration.ofMinutes(15)));

        // Reference data - longer TTL
        cacheConfigurations.put(CACHE_GENRES,
            defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put(CACHE_MOODS,
            defaultConfig.entryTtl(Duration.ofHours(24)));

        // Stats cache - short TTL for freshness
        cacheConfigurations.put(CACHE_STATS,
            defaultConfig.entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }
}
