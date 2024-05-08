package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetStatisticDto {

    private String start;

    private String end;

    private List<String> uris;

    private boolean unique;
}
