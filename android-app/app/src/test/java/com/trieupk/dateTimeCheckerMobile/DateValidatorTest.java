package com.trieupk.dateTimeCheckerMobile;

import org.junit.Test;
import static org.junit.Assert.*;

public class DateValidatorTest {

    private final DateValidator validator = new DateValidator();

    @Test
    public void testValidDate() {
        assertTrue(validator.isValidDate(29, 2, 2024));
        assertFalse(validator.isValidDate(29, 2, 2023));
    }

    @Test
    public void testValidationMessage() {
        assertEquals("29/02/2024 is correct date time!", validator.getValidationMessage("29", "2", "2024"));
        assertEquals("29/02/2023 is not a valid date!", validator.getValidationMessage("29", "2", "2023"));
        assertEquals("Invalid input! Please enter integers only.", validator.getValidationMessage("abc", "2", "2023"));
    }
}
