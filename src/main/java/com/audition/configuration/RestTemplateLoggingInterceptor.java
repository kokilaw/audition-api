package com.audition.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
        final ClientHttpRequestExecution execution)
        throws IOException {
        logRequest(request, body);
        final ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(final HttpRequest request, final byte[] body) throws IOException {
        if (LOG.isInfoEnabled()) {
            LOG.info("--- log request start ---");
            LOG.info("URI: {}", request.getURI());
            LOG.info("Method: {}", request.getMethod());
            LOG.info("Headers: {}", request.getHeaders());
            LOG.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
            LOG.info("--- log request end ---");
        }
    }

    private void logResponse(final ClientHttpResponse response) throws IOException {
        if (LOG.isInfoEnabled()) {
            LOG.info("--- log response start ---");
            LOG.info("Status code: {}", response.getStatusCode());
            LOG.info("Status text: {}", response.getStatusText());
            LOG.info("Headers: {}", response.getHeaders());
            LOG.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            LOG.info("--- log response end ---");
        }
    }

}
