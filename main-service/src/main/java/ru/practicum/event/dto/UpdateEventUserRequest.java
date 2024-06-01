package ru.practicum.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.enums.EventUserAction;
import ru.practicum.validation.annotation.ValidLocalDateTime;
import javax.validation.constraints.*;

@Getter
@Setter
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000)
    @NotBlank(message = "annotation can not be null or blank")
    private String annotation;

    @Positive
    @NotNull(message = "category can not be null")
    private Integer category;

    @Size(min = 20, max = 7000)
    @NotBlank(message = "description can not be null or blank")
    private String description;

    @ValidLocalDateTime
    @NotBlank(message = "eventDate can not be null or blank")
    private String eventDate;

    @NotNull(message = "location can not be null")
    private LocationDto location;

    @NotNull(message = "paid can not be null")
    private Boolean paid;

    @PositiveOrZero
    @NotNull(message = "participantLimit can not be null")
    private Integer participantLimit;

    @NotNull(message = "requestModeration can not be null")
    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    @NotBlank(message = "title can not be null or blank")
    private String title;

    @NotBlank(message = "stateAction can not be null")
    private EventUserAction stateAction;
}
