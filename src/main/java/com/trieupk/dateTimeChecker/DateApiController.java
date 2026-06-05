package com.trieupk.dateTimeChecker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DateApiController {

    private final DateValidator dateValidator = new DateValidator();

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkDate(
            @RequestParam("day") String day,
            @RequestParam("month") String month,
            @RequestParam("year") String year) {

        String resultMessage = dateValidator.getValidationMessage(day, month, year);
        boolean isValid = resultMessage.contains("is correct date time!");

        Map<String, Object> response = new HashMap<>();
        response.put("message", resultMessage);
        response.put("isValid", isValid);
        response.put("day", day);
        response.put("month", month);
        response.put("year", year);

        if (resultMessage.equals("Invalid input! Please enter integers only.")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
