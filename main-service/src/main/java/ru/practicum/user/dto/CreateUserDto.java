package ru.practicum.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
