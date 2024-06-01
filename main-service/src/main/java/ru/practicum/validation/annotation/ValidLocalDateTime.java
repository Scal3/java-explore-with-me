package ru.practicum.validation.annotation;

import ru.practicum.validation.LocalDateTimeValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocalDateTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocalDateTime {
    String message() default "Invalid date format. Expected format is yyyy-MM-dd HH:mm:ss";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String format() default "yyyy-MM-dd HH:mm:ss";
}
