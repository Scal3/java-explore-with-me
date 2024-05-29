package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.GetCategoriesDto;
import ru.practicum.category.service.CategoryService;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Entering getCategories: from={}, size={}", from, size);
        GetCategoriesDto dto = GetCategoriesDto.builder()
                        .from(from)
                        .size(size)
                        .build();
        List<CategoryDto> categoryDtos = categoryService.getCategories(dto);
        log.info("Exiting getCategories");

        return categoryDtos;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Positive long catId) {
        log.info("Entering getCategoryById: catId={}", catId);
        CategoryDto categoryDto = categoryService.getCategoryById(catId);
        log.info("Exiting getCategoryById");

        return categoryDto;
    }
}
