package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;

    private String name;

    private String email;
}
