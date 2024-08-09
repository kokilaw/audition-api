package com.audition.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "audition-integration-api")
public class ExternalApiConfiguration {

    private String baseUrl;
    private String getPostsPath;
    private String getSinglePostPath;
    private String getPostCommentsPath;
    private String getCommentsByPost;

}
