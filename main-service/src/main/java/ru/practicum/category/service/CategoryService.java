package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.GetCategoriesDto;
import ru.practicum.category.dto.NewCategoryDto;
import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto dto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(CategoryDto dto);

    List<CategoryDto> getCategories(GetCategoriesDto dto);

    CategoryDto getCategoryById(long categoryId);
}
