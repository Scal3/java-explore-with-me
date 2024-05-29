package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.GetCategoriesDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto dto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(UpdateCategoryDto dto);

    List<CategoryDto> getCategories(GetCategoriesDto dto);

    CategoryDto getCategoryById(long categoryId);
}
