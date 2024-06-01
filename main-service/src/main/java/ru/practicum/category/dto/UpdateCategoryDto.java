package ru.practicum.category.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateCategoryDto {

    private Long id;

    @NotBlank(message = "name can not be null or blank")
    private String name;
}
