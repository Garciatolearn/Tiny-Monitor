package org.garcia.monitor.server.tests;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Test {
    @Value("${spring.application.name}")
    String name;
}
