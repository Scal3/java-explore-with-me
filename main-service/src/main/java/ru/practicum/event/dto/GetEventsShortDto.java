package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.enums.EventSort;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsShortDto {

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private String rangeStart;

    private String rangeEnd;

    private Boolean onlyAvailable;

    private EventSort sort;

    private Integer from;

    private Integer size;
}
