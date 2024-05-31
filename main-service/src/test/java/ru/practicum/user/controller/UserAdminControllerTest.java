package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserAdminControllerTest {

    private static final String USER_URL = "/admin/users";

    private final ObjectMapper mapper;

    private final MockMvc mvc;

    @MockBean
    private final UserService userServiceMock;

    @Test
    void getUsersInfo() throws Exception {
        List<UserDto> dtos = List.of(new UserDto(1L, "user", "user@email"));

        when(userServiceMock.getUsersInfo(any()))
                .thenReturn(dtos);

        mvc.perform(get(USER_URL)
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(dtos.get(0).getId()));
    }

    @Test
    void createUser() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("user1");
        dto.setEmail("user1@email");

        UserDto userDto = UserDto.builder()
                        .id(1L)
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .build();

        when(userServiceMock.createUser(any())).thenReturn(userDto);

        mvc.perform(post(USER_URL)
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete(USER_URL + "/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userServiceMock, times(1)).deleteUserById(anyLong());
    }
}