package ru.practicum.event.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Double lat;

    private Double lon;
}
