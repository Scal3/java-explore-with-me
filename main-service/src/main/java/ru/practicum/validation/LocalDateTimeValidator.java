package ru.practicum.validation;

import ru.practicum.validation.annotation.ValidLocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    private String dateFormat;

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        this.dateFormat = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDateTime.parse(value, formatter);

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

