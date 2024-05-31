package ru.practicum.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.service.CategoryServiceImpl;
import java.nio.charset.StandardCharsets;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryAdminController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryAdminControllerTest {

    private static final String CATEGORY_URL = "/admin/categories";

    private final ObjectMapper mapper;

    private final MockMvc mvc;

    @MockBean
    private final CategoryServiceImpl categoryServiceMock;

    @Test
    void createCategory() throws Exception {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName("category");

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(newCategoryDto.getName())
                .build();

        when(categoryServiceMock.createCategory(any())).thenReturn(categoryDto);

        mvc.perform(post(CATEGORY_URL)
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteCategoryById() throws Exception {
        mvc.perform(delete(CATEGORY_URL + "/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryServiceMock, times(1)).deleteCategory(anyLong());
    }

    @Test
    void updateCategory() throws Exception {
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setName("updated");

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(updateCategoryDto.getName())
                .build();

        when(categoryServiceMock.updateCategory(any())).thenReturn(categoryDto);

        mvc.perform(patch(CATEGORY_URL + "/1")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}