package ru.practicum.user.service;

import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.GetUsersInfoDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(long userId);

    UserDto createUser(CreateUserDto dto);

    void deleteUserById(long userId);

    List<UserDto> getUsersInfo(GetUsersInfoDto dto);
}
