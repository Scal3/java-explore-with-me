package ru.practicum.category.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateCategoryDto {

    private Long id;

    @NotBlank
    private String name;
}
