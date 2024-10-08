package com.audition.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Setter
@Component
public class AuditionLogger {

    @Autowired
    private ObjectMapper objectMapper;

    public static void info(final Logger logger, final String message, final Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public void debug(final Logger logger, final String message, final Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public void warn(final Logger logger, final String message, final Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    public void error(final Logger logger, final String message, final Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    public void logErrorWithException(final Logger logger, final String message, final Exception e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    public void logStandardProblemDetail(final Logger logger, final ProblemDetail problemDetail, final Exception e) {
        if (logger.isErrorEnabled()) {
            final var message = createStandardProblemDetailMessage(problemDetail);
            logger.error(message, e);
        }
    }

    public void logHttpStatusCodeError(final Logger logger, final String message, final Integer errorCode) {
        if (logger.isErrorEnabled()) {
            logger.error(createBasicErrorResponseMessage(errorCode, message) + "\n");
        }
    }

    @SneakyThrows
    private String createStandardProblemDetailMessage(final ProblemDetail standardProblemDetail) {
        return objectMapper.writeValueAsString(standardProblemDetail);
    }

    @SneakyThrows
    private String createBasicErrorResponseMessage(final Integer errorCode, final String message) {
        return objectMapper.writeValueAsString(Map.of("errorCode", errorCode, "message", message));
    }
}
