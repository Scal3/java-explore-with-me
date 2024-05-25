package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.GetUsersInfoDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/admin/users")
@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getUsersInfo(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Entering getUsersInfo: ids={}, from={}, size={}", ids, from, size);
        GetUsersInfoDto getUsersInfoDto = GetUsersInfoDto.builder()
                .ids(ids)
                .from(from)
                .size(size)
                .build();
        List<UserDto> userDtos = userService.getUsersInfo(getUsersInfoDto);
        log.info("Exiting getUsersInfo");

        return userDtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto createUser(@RequestBody @Valid CreateUserDto dto) {
        log.info("Entering createUser: CreateUserDto = {}", dto);
        UserDto userDto = userService.createUser(dto);
        log.info("Exiting createUser");

        return userDto;

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive long userId) {
        log.info("Entering deleteUser: userId = {}", userId);
        userService.deleteUserById(userId);
        log.info("Exiting deleteUser");
    }
}
