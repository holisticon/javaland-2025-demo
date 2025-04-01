package de.holisticon.talk.reactivepipeline.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application")
@Component
@Data
public class ApplicationProperties {

    private String fileName;
}
