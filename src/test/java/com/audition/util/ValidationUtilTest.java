package com.audition.util;

import static com.audition.Constants.ErrorTitles.INVALID_PARAMETER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.audition.common.exception.SystemException;
import org.junit.jupiter.api.Test;

class ValidationUtilTest {

    @Test
    void givenInputWhenEmptyErrorIsThrown() {
        final String errorMessage = "ERROR_MESSAGE";
        final SystemException exception = assertThrows(SystemException.class,
            () -> ValidationUtil.isValidNumber("", errorMessage));
        assertEquals(exception.getDetail(), errorMessage);
        assertEquals(exception.getTitle(), INVALID_PARAMETER_ERROR);
    }

    @Test
    void givenInputWhenIsNotNumberErrorIsThrown() {
        final String errorMessage = "ERROR_MESSAGE";
        final SystemException exception = assertThrows(SystemException.class,
            () -> ValidationUtil.isValidNumber("878asdas", errorMessage));
        assertEquals(exception.getDetail(), errorMessage);
        assertEquals(exception.getTitle(), INVALID_PARAMETER_ERROR);
    }

}