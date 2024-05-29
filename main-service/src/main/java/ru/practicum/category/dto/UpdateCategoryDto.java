package ru.practicum.category.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class UpdateCategoryDto {

    @NotNull
    @Positive
    private Long id;

    @NotBlank
    private String name;
}
