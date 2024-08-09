package com.audition.util;

import static com.audition.Constants.ErrorTitles.INVALID_PARAMETER_ERROR;

import com.audition.common.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;

public class ValidationUtil {

    public static void isValidNumber(
        final String parameter,
        final String errorMessage) {
        isNotEmpty(parameter, errorMessage);
        if (!NumberUtils.isCreatable(parameter)) {
            throw new SystemException(
                errorMessage,
                INVALID_PARAMETER_ERROR,
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    private static void isNotEmpty(String parameter, String errorMessage) {
        if (StringUtils.isEmpty(parameter)) {
            throw new SystemException(
                errorMessage,
                INVALID_PARAMETER_ERROR,
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

}
