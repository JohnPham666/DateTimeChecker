package com.trieupk.dateTimeChecker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DateValidator}.
 * Contains 15 test cases for CheckDate (3 mẫu + 12 sinh: 9N, 2A, 1B)
 * and 15 test cases for DayInMonth (4 mẫu + 11 sinh: 8N, 2A, 1B).
 */
class DateValidatorTest {

    private DateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DateValidator();
    }

    // ==========================================
    // 1. CheckDate (isValidDate): 15 Test Cases
    // (3 samples + 12 generated: 9N, 2A, 1B)
    // ==========================================

    // --- 3 Sample Cases ---
    @Test
    @DisplayName("CheckDate 1 (Sample N): 29/2/2000 - Normal (Leap year century)")
    void testCheckDate_29_2_2000() {
        assertTrue(validator.isValidDate(29, 2, 2000));
    }

    @Test
    @DisplayName("CheckDate 2 (Sample A): 29/2/2009 - Abnormal (Not a leap year)")
    void testCheckDate_29_2_2009() {
        assertFalse(validator.isValidDate(29, 2, 2009));
    }

    @Test
    @DisplayName("CheckDate 3 (Sample A): 31/2/2020 - Abnormal (Feb does not have 31 days)")
    void testCheckDate_31_2_2020() {
        assertFalse(validator.isValidDate(31, 2, 2020));
    }

    // --- 12 Generated Cases (9 Normal, 2 Abnormal, 1 Boundary) ---
    // 9 Normal:
    @Test
    @DisplayName("CheckDate 4 (Gen N): 29/2/2024 - Normal (Leap year)")
    void testCheckDate_29_2_2024() {
        assertTrue(validator.isValidDate(29, 2, 2024));
    }

    @Test
    @DisplayName("CheckDate 5 (Gen N): 28/2/2021 - Normal (Non-leap year)")
    void testCheckDate_28_2_2021() {
        assertTrue(validator.isValidDate(28, 2, 2021));
    }

    @Test
    @DisplayName("CheckDate 6 (Gen N): 15/6/2020 - Normal (Mid 30-day month)")
    void testCheckDate_15_6_2020() {
        assertTrue(validator.isValidDate(15, 6, 2020));
    }

    @Test
    @DisplayName("CheckDate 7 (Gen N): 15/7/2020 - Normal (Mid 31-day month)")
    void testCheckDate_15_7_2020() {
        assertTrue(validator.isValidDate(15, 7, 2020));
    }

    @Test
    @DisplayName("CheckDate 8 (Gen N): 31/1/2020 - Normal (End of 31-day month)")
    void testCheckDate_31_1_2020() {
        assertTrue(validator.isValidDate(31, 1, 2020));
    }

    @Test
    @DisplayName("CheckDate 9 (Gen N): 30/4/2020 - Normal (End of 30-day month)")
    void testCheckDate_30_4_2020() {
        assertTrue(validator.isValidDate(30, 4, 2020));
    }

    @Test
    @DisplayName("CheckDate 10 (Gen N): 1/6/2020 - Normal (Start of month)")
    void testCheckDate_1_6_2020() {
        assertTrue(validator.isValidDate(1, 6, 2020));
    }

    @Test
    @DisplayName("CheckDate 11 (Gen N): 31/12/2020 - Normal (End of year)")
    void testCheckDate_31_12_2020() {
        assertTrue(validator.isValidDate(31, 12, 2020));
    }

    @Test
    @DisplayName("CheckDate 12 (Gen N): 15/12/2021 - Normal (December mid-month)")
    void testCheckDate_15_12_2021() {
        assertTrue(validator.isValidDate(15, 12, 2021));
    }

    // 2 Abnormal:
    @Test
    @DisplayName("CheckDate 13 (Gen A): 30/2/2024 - Abnormal (Feb 30 does not exist)")
    void testCheckDate_30_2_2024() {
        assertFalse(validator.isValidDate(30, 2, 2024));
    }

    @Test
    @DisplayName("CheckDate 14 (Gen A): 31/4/2020 - Abnormal (April has 30 days)")
    void testCheckDate_31_4_2020() {
        assertFalse(validator.isValidDate(31, 4, 2020));
    }

    // 1 Boundary:
    @Test
    @DisplayName("CheckDate 15 (Gen B): 1/1/1000 - Boundary (Min Date)")
    void testCheckDate_1_1_1000() {
        assertTrue(validator.isValidDate(1, 1, 1000));
    }


    // ==========================================
    // 2. DayInMonth: 15 Test Cases
    // (4 samples + 11 generated: 8N, 2A, 1B)
    // ==========================================

    // --- 4 Sample Cases ---
    @Test
    @DisplayName("DayInMonth 1 (Sample N): Month 1, Year 2020 -> return 31")
    void testDayInMonth_1_2020() {
        assertEquals(31, validator.daysInMonth(1, 2020));
    }

    @Test
    @DisplayName("DayInMonth 2 (Sample N): Month 2, Year 2021 -> return 28")
    void testDayInMonth_2_2021() {
        assertEquals(28, validator.daysInMonth(2, 2021));
    }

    @Test
    @DisplayName("DayInMonth 3 (Sample N): Month 2, Year 2019 -> return 28")
    void testDayInMonth_2_2019() {
        assertEquals(28, validator.daysInMonth(2, 2019));
    }

    @Test
    @DisplayName("DayInMonth 4 (Sample A): Month 15, Year 2021 -> return 0")
    void testDayInMonth_15_2021() {
        assertEquals(0, validator.daysInMonth(15, 2021));
    }

    // --- 11 Generated Cases (8 Normal, 2 Abnormal, 1 Boundary) ---
    // 8 Normal:
    @Test
    @DisplayName("DayInMonth 5 (Gen N): Month 2, Year 2024 -> return 29 (Leap year)")
    void testDayInMonth_2_2024() {
        assertEquals(29, validator.daysInMonth(2, 2024));
    }

    @Test
    @DisplayName("DayInMonth 6 (Gen N): Month 2, Year 2000 -> return 29 (Century leap year)")
    void testDayInMonth_2_2000() {
        assertEquals(29, validator.daysInMonth(2, 2000));
    }

    @Test
    @DisplayName("DayInMonth 7 (Gen N): Month 2, Year 1900 -> return 28 (Century non-leap)")
    void testDayInMonth_2_1900() {
        assertEquals(28, validator.daysInMonth(2, 1900));
    }

    @Test
    @DisplayName("DayInMonth 8 (Gen N): Month 4, Year 2020 -> return 30")
    void testDayInMonth_4_2020() {
        assertEquals(30, validator.daysInMonth(4, 2020));
    }

    @Test
    @DisplayName("DayInMonth 9 (Gen N): Month 12, Year 2021 -> return 31")
    void testDayInMonth_12_2021() {
        assertEquals(31, validator.daysInMonth(12, 2021));
    }

    @Test
    @DisplayName("DayInMonth 10 (Gen N): Month 7, Year 2020 -> return 31")
    void testDayInMonth_7_2020() {
        assertEquals(31, validator.daysInMonth(7, 2020));
    }

    @Test
    @DisplayName("DayInMonth 11 (Gen N): Month 6, Year 2020 -> return 30")
    void testDayInMonth_6_2020() {
        assertEquals(30, validator.daysInMonth(6, 2020));
    }

    @Test
    @DisplayName("DayInMonth 12 (Gen N): Month 9, Year 2020 -> return 30")
    void testDayInMonth_9_2020() {
        assertEquals(30, validator.daysInMonth(9, 2020));
    }

    // 2 Abnormal:
    @Test
    @DisplayName("DayInMonth 13 (Gen A): Month null, Year 2021 -> return 0")
    void testDayInMonth_null_2021() {
        assertEquals(0, validator.daysInMonth(null, 2021));
    }

    @Test
    @DisplayName("DayInMonth 14 (Gen A): Month 5, Year null -> return 0")
    void testDayInMonth_5_null() {
        assertEquals(0, validator.daysInMonth(5, null));
    }

    // 1 Boundary:
    @Test
    @DisplayName("DayInMonth 15 (Gen B): Month 1, Year 1000 -> return 31 (Boundary min year)")
    void testDayInMonth_1_1000() {
        assertEquals(31, validator.daysInMonth(1, 1000));
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
