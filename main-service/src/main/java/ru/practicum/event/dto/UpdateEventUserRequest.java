package ru.practicum.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.enums.EventUserAction;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateEventUserRequest {

    private String annotation;

    private Integer category;

    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;

    @NotBlank(message = "stateAction can not be null")
    private EventUserAction stateAction;
}
