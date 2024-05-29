package ru.practicum.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.GetUsersInfoDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entity.UserEntity;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository repositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Test
    void createUser_normalCase_thenReturnUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("user");
        createUserDto.setEmail("user@email.com");

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .build();

        when(modelMapperMock.map(any(), any())).thenReturn(userDto);

        UserDto result = userService.createUser(createUserDto);

        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(repositoryMock, times(1))
                .save(any());
    }

    @Test
    void deleteUserById_normalCase_thenDeleteUser() {
        when(repositoryMock.findById(any())).thenReturn(Optional.of(new UserEntity()));

        userService.deleteUserById(1L);
        verify(repositoryMock, times(1))
                .deleteById(any());
    }

    @Test
    void deleteUserById_userIsNotFound_thenThrowNotFoundException() {
        when(repositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUserById(1L));
        verify(repositoryMock, never())
                .deleteById(any());
    }

    @Test
    void getUsersInfo_requestHasIdsList_thenReturnUserDtoListIncludesOnlyUsersWithIdFromIdsList() {
        GetUsersInfoDto getUsersInfoDto = GetUsersInfoDto.builder()
                .ids(List.of(1L, 2L))
                .from(0)
                .size(10)
                .build();

        List<UserEntity> userEntities = List.of(
                new UserEntity(1L, "user1", "email1"),
                new UserEntity(2L, "user2", "email2"));

        List<UserDto> userDtos = List.of(
                new UserDto(1L, "user1", "email1"),
                new UserDto(2L, "user2", "email2"));

        when(repositoryMock.findAllByIdIn(any(), any())).thenReturn(userEntities);
        when(modelMapperMock.map(any(), eq(new TypeToken<List<UserDto>>() {}.getType()))).thenReturn(userDtos);

        List<UserDto> result = userService.getUsersInfo(getUsersInfoDto);

        assertEquals(2, result.size());
    }

    @Test
    void getUsersInfo_requestDoesNotHaveIdsList_thenReturnUserDtoList() {
        GetUsersInfoDto getUsersInfoDto = GetUsersInfoDto.builder()
                .ids(null)
                .from(0)
                .size(10)
                .build();

        Pageable pageable = PageRequest.of(
                getUsersInfoDto.getFrom() / getUsersInfoDto.getSize(),
                getUsersInfoDto.getSize());

        Page<UserEntity> userEntities = new PageImpl<>(
                List.of(
                        new UserEntity(1L, "user1", "email1"),
                        new UserEntity(2L, "user2", "email2"))
        );

        List<UserDto> userDtos = List.of(
                new UserDto(1L, "user1", "email1"),
                new UserDto(2L, "user2", "email2"));

        when(repositoryMock.findAll(pageable)).thenReturn(userEntities);
        when(modelMapperMock.map(any(), eq(new TypeToken<List<UserDto>>() {}.getType()))).thenReturn(userDtos);

        List<UserDto> result = userService.getUsersInfo(getUsersInfoDto);

        assertEquals(2, result.size());
    }
}