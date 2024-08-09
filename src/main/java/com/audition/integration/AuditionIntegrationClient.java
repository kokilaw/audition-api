package com.audition.integration;

import com.audition.Constants.ErrorMessages;
import com.audition.common.exception.SystemException;
import com.audition.configuration.ExternalApiConfiguration;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Setter
@Component
public class AuditionIntegrationClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ExternalApiConfiguration externalApiConfiguration;

    public List<AuditionPost> getPosts() {
        final String requestUrl = externalApiConfiguration.getBaseUrl()
            .concat(externalApiConfiguration.getGetPostsPath());

        try {
            final AuditionPost[] body = restTemplate.getForEntity(requestUrl, AuditionPost[].class).getBody();
            if (body != null) {
                return Arrays.asList(body);
            }
            return Collections.emptyList();
        } catch (final HttpClientErrorException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error occurred while executing getPosts - requestUrl[{}] statusCode[{}] responseBody[{}] - ",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    e
                );
            }
            throw new SystemException(ErrorMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    public AuditionPost getPostById(final String id) {
        final String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetSinglePostPath()),
            id
        );
        try {
            return restTemplate.getForEntity(requestUrl, AuditionPost.class).getBody();
        } catch (final HttpClientErrorException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Error occurred while executing getPostById - requestUrl[{}] statusCode[{}] responseBody[{}] - ",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    e
                );
            }
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(
                    String.format("Cannot find a Post with id - %s", id),
                    "Resource Not Found",
                    HttpStatus.NOT_FOUND.value(),
                    e
                );
            } else {
                throw new SystemException(ErrorMessages.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    public AuditionPost getPostByIdIncludingComments(final String id) {
        final String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetPostCommentsPath()),
            id
        );
        final AuditionPost auditionPost = this.getPostById(id);
        try {
            final AuditionPostComment[] body = restTemplate.getForEntity(requestUrl,
                AuditionPostComment[].class).getBody();
            if (body != null) {
                auditionPost.setComments(Arrays.asList(body));
            }
            return auditionPost;
        } catch (final HttpClientErrorException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Error occurred while executing getPostByIdIncludingComments - requestUrl[{}] statusCode[{}] responseBody[{}] -",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    e
                );
            }
            throw new SystemException(ErrorMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    public List<AuditionPostComment> getCommentsByPost(final String id) {
        final String requestUrl = String.format(
            externalApiConfiguration.getBaseUrl().concat(externalApiConfiguration.getGetCommentsByPost()),
            id
        );
        try {
            final AuditionPostComment[] body = restTemplate.getForEntity(requestUrl,
                AuditionPostComment[].class).getBody();
            if (body != null) {
                return Arrays.asList(body);
            } else {
                return Collections.emptyList();
            }
        } catch (final HttpClientErrorException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Error occurred while executing getCommentsByPost - requestUrl[{}] statusCode[{}] responseBody[{}] - ",
                    requestUrl,
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString(),
                    e
                );
            }
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(
                    String.format("Cannot find a Post with id - %s", id),
                    "Resource Not Found",
                    HttpStatus.NOT_FOUND.value(),
                    e
                );
            } else {
                throw new SystemException(ErrorMessages.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }
}
