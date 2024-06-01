package ru.practicum.category.service;

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
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.entity.CategoryEntity;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.error.NotFoundException;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Test
    void createCategory_normalCase_thenReturnCategoryDto() {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName("new category");

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(newCategoryDto.getName())
                .build();

        when(modelMapperMock.map(any(), any())).thenReturn(categoryDto);

        CategoryDto result = categoryService.createCategory(newCategoryDto);

        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        verify(categoryRepositoryMock, times(1))
                .save(any());
    }

    @Test
    void deleteCategory_normalCase_thenDeleteCategoryById() {
        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(new CategoryEntity()));

        categoryService.deleteCategory(1L);

        verify(categoryRepositoryMock, times(1))
                .deleteById(anyLong());
    }

    @Test
    void deleteCategory_categoryIsNotFound_thenThrowNotFoundException() {
        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategory(1L));

        verify(categoryRepositoryMock, never())
                .deleteById(anyLong());
    }

    @Test
    void updateCategory_normalCase_thenReturnCategoryDto() {
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(1L);
        updateCategoryDto.setName("updated");

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(updateCategoryDto.getName())
                .build();

        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(new CategoryEntity()));
        when(modelMapperMock.map(any(), any())).thenReturn(categoryDto);

        CategoryDto result = categoryService.updateCategory(updateCategoryDto);

        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        verify(categoryRepositoryMock, times(1))
                .save(any());
    }

    @Test
    void updateCategory_categoryIsNotFound_thenThrowNotFoundException() {
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(1L);
        updateCategoryDto.setName("updated");

        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.updateCategory(updateCategoryDto));

        verify(categoryRepositoryMock, never())
                .save(any());
    }

    @Test
    void getCategories_normalCase_thenReturnListCategoryDto() {
        Pageable pageable = PageRequest.of(0 / 10, 10);
        Page<CategoryEntity> categoryEntities = new PageImpl<>(
                List.of(new CategoryEntity(), new CategoryEntity())
        );
        List<CategoryDto> categoryDtos = List.of(new CategoryDto());

        when(categoryRepositoryMock.findAll(pageable)).thenReturn(categoryEntities);
        when(modelMapperMock.map(any(), eq(new TypeToken<List<CategoryDto>>() {}.getType())))
                .thenReturn(categoryDtos);

        List<CategoryDto> result = categoryService.getCategories(pageable);

        assertEquals(1, result.size());
    }

    @Test
    void getCategoryById_normalCase_thenReturnCategoryDto() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("category")
                .build();

        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(new CategoryEntity()));

        when(modelMapperMock.map(any(), any())).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(1L);

        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
    }

    @Test
    void getCategoryById_categoryIsNotFound_thenThrowNotFoundException() {
        when(categoryRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
    }
}