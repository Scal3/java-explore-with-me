package ru.practicum.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.enums.RequestState;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private RequestState status;
}
