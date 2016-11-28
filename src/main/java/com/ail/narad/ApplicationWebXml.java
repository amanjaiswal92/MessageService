package com.ail.narad;

import com.ail.narad.config.Constants;
import com.ail.narad.config.Initialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * This is a helper Java class that provides an alternative to creating a web.xml.
 */
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Autowired
    Initialize initialize;
    private final Logger log = LoggerFactory.getLogger(ApplicationWebXml.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.profiles(addDefaultProfile())
            .sources(Application.class);
    }

    /**
     * Set a default profile if it has not been set.
     * <p>
     * Please use -Dspring.profiles.active=dev
     * </p>
     */
    private String addDefaultProfile() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null) {
            log.info("Running with Spring profile(s) : {}", profile);
            return profile;
        }

        log.warn("No Spring profile configured, running with default configuration");
        return Constants.SPRING_PROFILE_DEVELOPMENT;
    }


}
