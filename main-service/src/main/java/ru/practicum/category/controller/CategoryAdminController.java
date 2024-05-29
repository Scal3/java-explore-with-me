package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.service.CategoryService;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody NewCategoryDto dto) {
        log.info("Entering createCategory: NewCategoryDto = {}", dto);
        CategoryDto categoryDto = categoryService.createCategory(dto);
        log.info("Exiting createCategory");

        return categoryDto;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategoryById(@PathVariable @Positive long catId) {
        log.info("Entering deleteCategoryById: catId = {}", catId);
        CategoryDto categoryDto = categoryService.getCategoryById(catId);
        log.info("Exiting deleteCategoryById");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @RequestBody UpdateCategoryDto dto,
            @PathVariable @Positive long catId
    ) {
        log.info("Entering updateCategory: UpdateCategoryDto = {}, catId = {}", dto, catId);
        dto.setId(catId);
        CategoryDto categoryDto = categoryService.updateCategory(dto);
        log.info("Exiting updateCategory");

        return categoryDto;
    }
}
