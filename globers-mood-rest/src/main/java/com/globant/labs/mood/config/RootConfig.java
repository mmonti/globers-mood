package com.globant.labs.mood.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.globant.labs.mood.events",
        "com.globant.labs.mood.exception.mapping",
        "com.globant.labs.mood.resources"
})
@Import({PropertiesConfig.class, RepositoryConfig.class, ServiceConfig.class, SecurityConfig.class})
public class RootConfig {

    @Bean
    public MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter() {
        return new MappingJacksonHttpMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
