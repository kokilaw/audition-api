package com.audition.configuration;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector implements Filter {

    @Autowired
    private transient Tracer tracer;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(
        final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain
    ) throws IOException, ServletException {
        if (response instanceof HttpServletResponse httpServletResponse) {
            final Span currentSpan = tracer.spanBuilder().start();
            httpServletResponse.setHeader("trace-id", currentSpan.context().traceId());
            httpServletResponse.setHeader("span-id", currentSpan.context().spanId());
            currentSpan.end();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
