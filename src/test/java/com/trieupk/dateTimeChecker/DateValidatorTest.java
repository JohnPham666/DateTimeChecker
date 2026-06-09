package com.trieupk.dateTimeChecker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DateValidator}.
 * Contains 15 test cases for CheckDate and 15 test cases for DayInMonth as
 * requested.
 */
class DateValidatorTest {

    private DateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DateValidator();
    }

    // ==========================================
    // 1. CheckDate (isValidDate): 15 Test Cases
    // ==========================================

    @Test
    @DisplayName("CheckDate 1: 29/2/2000 - Normal (Leap year century)")
    void testCheckDate_29_2_2000() {
        assertTrue(validator.isValidDate(29, 2, 2000));
    }

    @Test
    @DisplayName("CheckDate 2: 29/2/2009 - Abnormal (Not a leap year)")
    void testCheckDate_29_2_2009() {
        assertFalse(validator.isValidDate(29, 2, 2009));
    }

    @Test
    @DisplayName("CheckDate 3: 31/2/2020 - Abnormal (Feb does not have 31 days)")
    void testCheckDate_31_2_2020() {
        assertFalse(validator.isValidDate(31, 2, 2020));
    }

    @Test
    @DisplayName("CheckDate 4: 15/6/2020 - Normal")
    void testCheckDate_15_6_2020() {
        assertTrue(validator.isValidDate(15, 6, 2020));
    }

    @Test
    @DisplayName("CheckDate 5: 31/1/2020 - Normal (End of month 31 days)")
    void testCheckDate_31_1_2020() {
        assertTrue(validator.isValidDate(31, 1, 2020));
    }

    @Test
    @DisplayName("CheckDate 6: 31/4/2020 - Abnormal (April has 30 days)")
    void testCheckDate_31_4_2020() {
        assertFalse(validator.isValidDate(31, 4, 2020));
    }

    @Test
    @DisplayName("CheckDate 7: 1/1/1000 - Boundary (Min Date)")
    void testCheckDate_1_1_1000() {
        assertTrue(validator.isValidDate(1, 1, 1000));
    }

    @Test
    @DisplayName("CheckDate 8: 31/12/3000 - Boundary (Max Date)")
    void testCheckDate_31_12_3000() {
        assertTrue(validator.isValidDate(31, 12, 3000));
    }

    @Test
    @DisplayName("CheckDate 9: 0/1/2020 - Boundary (Day below min)")
    void testCheckDate_0_1_2020() {
        assertFalse(validator.isValidDate(0, 1, 2020));
    }

    @Test
    @DisplayName("CheckDate 10: 32/1/2020 - Boundary (Day above max)")
    void testCheckDate_32_1_2020() {
        assertFalse(validator.isValidDate(32, 1, 2020));
    }

    @Test
    @DisplayName("CheckDate 11: 15/0/2020 - Boundary (Month below min)")
    void testCheckDate_15_0_2020() {
        assertFalse(validator.isValidDate(15, 0, 2020));
    }

    @Test
    @DisplayName("CheckDate 12: 15/13/2020 - Boundary (Month above max)")
    void testCheckDate_15_13_2020() {
        assertFalse(validator.isValidDate(15, 13, 2020));
    }

    @Test
    @DisplayName("CheckDate 13: 15/6/999 - Boundary (Year below min)")
    void testCheckDate_15_6_999() {
        assertFalse(validator.isValidDate(15, 6, 999));
    }

    @Test
    @DisplayName("CheckDate 14: 15/6/3001 - Boundary (Year above max)")
    void testCheckDate_15_6_3001() {
        assertFalse(validator.isValidDate(15, 6, 3001));
    }

    @Test
    @DisplayName("CheckDate 15: 30/2/2024 - Abnormal (Feb 30 does not exist)")
    void testCheckDate_30_2_2024() {
        assertFalse(validator.isValidDate(30, 2, 2024));
    }

    // ==========================================
    // 2. DayInMonth: 15 Test Cases
    // ==========================================

    @Test
    @DisplayName("DayInMonth 1: Month 1, Year 2020 -> return 31")
    void testDayInMonth_1_2020() {
        assertEquals(31, validator.daysInMonth(1, 2020));
    }

    @Test
    @DisplayName("DayInMonth 2: Month 2, Year 2021 -> return 28")
    void testDayInMonth_2_2021() {
        assertEquals(28, validator.daysInMonth(2, 2021));
    }

    @Test
    @DisplayName("DayInMonth 3: Month 2, Year 2019 -> return 28")
    void testDayInMonth_2_2019() {
        assertEquals(28, validator.daysInMonth(2, 2019));
    }

    @Test
    @DisplayName("DayInMonth 4: Month 15, Year 2021 -> return 0 (Abnormal)")
    void testDayInMonth_15_2021() {
        assertEquals(0, validator.daysInMonth(15, 2021));
    }

    @Test
    @DisplayName("DayInMonth 5: Month null, Year 2021 -> return 0 (Abnormal)")
    void testDayInMonth_null_2021() {
        assertEquals(0, validator.daysInMonth(null, 2021));
    }

    @Test
    @DisplayName("DayInMonth 6: Month 2, Year 2024 -> return 29 (Leap year)")
    void testDayInMonth_2_2024() {
        assertEquals(29, validator.daysInMonth(2, 2024));
    }

    @Test
    @DisplayName("DayInMonth 7: Month 2, Year 2000 -> return 29 (Century leap year)")
    void testDayInMonth_2_2000() {
        assertEquals(29, validator.daysInMonth(2, 2000));
    }

    @Test
    @DisplayName("DayInMonth 8: Month 2, Year 1900 -> return 28 (Century non-leap year)")
    void testDayInMonth_2_1900() {
        assertEquals(28, validator.daysInMonth(2, 1900));
    }

    @Test
    @DisplayName("DayInMonth 9: Month 4, Year 2020 -> return 30 (Normal)")
    void testDayInMonth_4_2020() {
        assertEquals(30, validator.daysInMonth(4, 2020));
    }

    @Test
    @DisplayName("DayInMonth 10: Month 0, Year 2020 -> return 0 (Boundary min-1)")
    void testDayInMonth_0_2020() {
        assertEquals(0, validator.daysInMonth(0, 2020));
    }

    @Test
    @DisplayName("DayInMonth 11: Month 12, Year 2020 -> return 31 (Boundary max)")
    void testDayInMonth_12_2020() {
        assertEquals(31, validator.daysInMonth(12, 2020));
    }

    @Test
    @DisplayName("DayInMonth 12: Month 13, Year 2020 -> return 0 (Boundary max+1)")
    void testDayInMonth_13_2020() {
        assertEquals(0, validator.daysInMonth(13, 2020));
    }

    @Test
    @DisplayName("DayInMonth 13: Month 5, Year 999 -> return 0 (Boundary year min-1)")
    void testDayInMonth_5_999() {
        assertEquals(0, validator.daysInMonth(5, 999));
    }

    @Test
    @DisplayName("DayInMonth 14: Month 5, Year 3001 -> return 0 (Boundary year max+1)")
    void testDayInMonth_5_3001() {
        assertEquals(0, validator.daysInMonth(5, 3001));
    }

    @Test
    @DisplayName("DayInMonth 15: Month 5, Year null -> return 0 (Abnormal)")
    void testDayInMonth_5_null() {
        assertEquals(0, validator.daysInMonth(5, null));
    }

    // ==========================================
    // Remaining existing validation message tests
    // ==========================================

    @Test
    @DisplayName("Message: valid date returns success message")
    void testMessageValidDate() {
        String result = validator.getValidationMessage("29", "2", "2024");
        assertEquals("29/02/2024 is correct date time!", result);
    }

    @Test
    @DisplayName("Message: invalid date returns error message")
    void testMessageInvalidDate() {
        String result = validator.getValidationMessage("30", "2", "2024");
        assertEquals("30/02/2024 is not a valid date!", result);
    }

    @Test
    @DisplayName("Message: non-integer input returns parse error")
    void testMessageNonInteger() {
        String result = validator.getValidationMessage("abc", "2", "2024");
        assertEquals("Invalid input! Please enter integers only.", result);
    }

    @Test
    @DisplayName("Message: out-of-range input returns range error")
    void testMessageOutOfRange() {
        String result = validator.getValidationMessage("15", "6", "999");
        assertEquals("Day must be 1-31, Month must be 1-12, Year must be 1000-3000.", result);
    }
}
