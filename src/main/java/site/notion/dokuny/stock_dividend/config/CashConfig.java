package site.notion.dokuny.stock_dividend.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import site.notion.dokuny.stock_dividend.utils.CustomLocalDateTimeDeserializer;
import site.notion.dokuny.stock_dividend.utils.CustomLocalDateTimeSerializer;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CashConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	private ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDateTime.class,new CustomLocalDateTimeSerializer());
		module.addDeserializer(LocalDateTime.class,new CustomLocalDateTimeDeserializer());
		mapper.registerModule(module);
		GenericJackson2JsonRedisSerializer.registerNullValueSerializer(mapper,null);

		StdTypeResolverBuilder typer = new DefaultTypeResolverBuilder(DefaultTyping.EVERYTHING,
			mapper.getPolymorphicTypeValidator());
		typer = typer.init(JsonTypeInfo.Id.CLASS, null);
		typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);

		mapper.setDefaultTyping(typer);


		return mapper;
	}


	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host,port);
	}

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer(objectMapper())));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
			redisConnectionFactory).cacheDefaults(conf).build();
	}

}
