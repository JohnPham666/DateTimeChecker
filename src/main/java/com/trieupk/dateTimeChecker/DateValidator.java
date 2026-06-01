package com.trieupk.dateTimeChecker;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Utility class for validating date inputs.
 * Validates that Day is in [1, 31], Month is in [1, 12], Year is in [1000,
 * 3000],
 * and that the date actually exists in the Gregorian calendar.
 */
public class DateValidator {

    public static final int MIN_DAY = 1;
    public static final int MAX_DAY = 31;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MIN_YEAR = 1000;
    public static final int MAX_YEAR = 3000;

    /**
     * Checks whether the given day, month, and year fall within the allowed ranges.
     *
     * @param day   the day value
     * @param month the month value
     * @param year  the year value
     * @return true if all values are within their respective ranges
     */
    public boolean isValidRange(int day, int month, int year) {
        return day >= MIN_DAY && day <= MAX_DAY
                && month >= MIN_MONTH && month <= MAX_MONTH
                && year >= MIN_YEAR && year <= MAX_YEAR;
    }

    /**
     * Checks whether the given day, month, and year form a valid date
     * in the Gregorian calendar. This method first checks ranges,
     * then verifies the date exists (e.g., Feb 29 only in leap years).
     *
     * @param day   the day value
     * @param month the month value
     * @param year  the year value
     * @return true if the date is valid
     */
    public boolean isValidDate(int day, int month, int year) {
        if (!isValidRange(day, month, year)) {
            return false;
        }
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    /**
     * Parses string inputs and returns a user-friendly validation message.
     *
     * @param dayStr   the day as a string
     * @param monthStr the month as a string
     * @param yearStr  the year as a string
     * @return a message indicating whether the date is valid or describing the
     *         error
     */
    public String getValidationMessage(String dayStr, String monthStr, String yearStr) {
        int day, month, year;

        // Parse inputs
        try {
            day = Integer.parseInt(dayStr.trim());
            month = Integer.parseInt(monthStr.trim());
            year = Integer.parseInt(yearStr.trim());
        } catch (NumberFormatException e) {
            return "Invalid input! Please enter integers only.";
        }

        // Check ranges
        if (!isValidRange(day, month, year)) {
            return "Day must be 1-31, Month must be 1-12, Year must be 1000-3000.";
        }

        // Format the date string as dd/mm/yyyy
        String dateStr = String.format("%02d/%02d/%04d", day, month, year);

        // Check if the date actually exists
        if (isValidDate(day, month, year)) {
            return dateStr + " is correct date time!";
        } else {
            return dateStr + " is not a valid date!";
        }
    }
}
