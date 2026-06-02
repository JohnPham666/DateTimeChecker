package com.trieupk.dateTimeCheckerMobile;

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
     */
    public boolean isValidRange(int day, int month, int year) {
        return day >= MIN_DAY && day <= MAX_DAY
                && month >= MIN_MONTH && month <= MAX_MONTH
                && year >= MIN_YEAR && year <= MAX_YEAR;
    }

    /**
     * Checks whether the given day, month, and year form a valid date
     * in the Gregorian calendar.
     */
    public boolean isValidDate(int day, int month, int year) {
        if (!isValidRange(day, month, year)) {
            return false;
        }
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION.SDK_INT) {
                LocalDate.of(year, month, day);
            }
            return true;
        } catch (DateTimeException e) {
            return false;
        } catch (NoClassDefFoundError e) {
            // Android fallback
            try {
                java.util.Calendar cal = new java.util.GregorianCalendar(year, month - 1, day);
                cal.setLenient(false);
                cal.getTime();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Parses string inputs and returns a user-friendly validation message.
     */
    public String getValidationMessage(String dayStr, String monthStr, String yearStr) {
        int day, month, year;

        try {
            day = Integer.parseInt(dayStr.trim());
            month = Integer.parseInt(monthStr.trim());
            year = Integer.parseInt(yearStr.trim());
        } catch (NumberFormatException e) {
            return "Invalid input! Please enter integers only.";
        }

        if (!isValidRange(day, month, year)) {
            return "Day must be 1-31, Month must be 1-12, Year must be 1000-3000.";
        }

        String dateStr = String.format("%02d/%02d/%04d", day, month, year);

        if (isValidDate(day, month, year)) {
            return dateStr + " is correct date time!";
        } else {
            return dateStr + " is not a valid date!";
        }
    }
}
