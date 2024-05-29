package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetCategoriesDto {

    private int from;

    private int size;
}
