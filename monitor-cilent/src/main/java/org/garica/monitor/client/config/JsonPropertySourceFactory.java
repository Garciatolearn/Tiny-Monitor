package org.garica.monitor.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JsonPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map o = objectMapper.readValue(resource.getInputStream(), Map.class );
        return new MapPropertySource("json-property", o);
    }
}
