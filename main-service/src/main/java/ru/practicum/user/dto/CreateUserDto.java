package ru.practicum.user.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank(message = "name can not be null or blank")
    private String name;

    @NotBlank(message = "email can not be null or blank")
    @Email(message = "email must be a valid email address")
    private String email;
}
