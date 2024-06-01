package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto dto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(UpdateCategoryDto dto);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(long categoryId);
}
