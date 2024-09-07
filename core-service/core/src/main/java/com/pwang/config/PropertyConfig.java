package com.pwang.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Puck Wang
 * @project core-service
 * @created 9/6/2024
 */

@Configuration
@PropertySource(value = {"classpath:application.yml"})
@Getter
public class PropertyConfig {
  @Value("${spring.ai.openai.api-key}")
  private String apiKey;
}
