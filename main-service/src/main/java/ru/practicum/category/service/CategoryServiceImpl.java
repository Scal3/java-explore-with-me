package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.GetCategoriesDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.entity.CategoryEntity;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.error.NotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        log.info("Entering createCategory: NewCategoryDto = {}", dto);
        CategoryEntity category = CategoryEntity.builder()
                .name(dto.getName())
                .build();

        categoryRepository.save(category);
        log.info("Exiting createCategory");

        return mapper.map(category, CategoryDto.class);
    }

    @Transactional
    @Override
    public void deleteCategory(long categoryId) {
        log.info("Entering deleteCategory: categoryId = {}", categoryId);

        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Category  with id=" + categoryId + " was not found");
        }

        categoryRepository.deleteById(categoryId);
        log.info("Exiting deleteCategory");
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(UpdateCategoryDto dto) {
        log.info("Entering updateCategory: UpdateCategoryDto = {}", dto);
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(dto.getId());

        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Category  with id=" + dto.getId() + " was not found");
        }

        CategoryEntity updatedCategory = optionalCategory.get();
        updatedCategory.setName(dto.getName());
        categoryRepository.save(updatedCategory);
        log.info("Exiting updateCategory");

        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(GetCategoriesDto dto) {
        log.info("Entering getCategories: GetCategoriesDto = {}", dto);
        Pageable pageable = PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize());
        List<CategoryEntity> categoryEntities = categoryRepository.findAll(pageable).toList();
        log.info("Exiting getCategories");

        return mapper.map(categoryEntities, new TypeToken<List<CategoryDto>>() {}.getType());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(long categoryId) {
        log.info("Entering getCategoryById: categoryId = {}", categoryId);
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Category  with id=" + categoryId + " was not found");
        }

        log.info("Exiting getCategoryById");

        return mapper.map(optionalCategory.get(), CategoryDto.class);
    }
}
