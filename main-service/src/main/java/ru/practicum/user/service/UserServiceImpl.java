package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.GetUsersInfoDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entity.UserEntity;
import ru.practicum.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long userId) {
        log.info("Entering getUserById: userId = {}", userId);
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (userEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "User with id=" + userId + " was not found");
        }

        UserEntity userEntity = userEntityOptional.get();
        log.info("Exiting getUserById");

        return mapper.map(userEntity, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto createUser(CreateUserDto dto) {
        log.info("Entering createUser: CreateUserDto = {}", dto);
        UserEntity userEntity = UserEntity.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();

        userRepository.save(userEntity);
        log.info("Exiting createUser");

        return mapper.map(userEntity, UserDto.class);
    }

    @Transactional
    @Override
    public void deleteUserById(long userId) {
        log.info("Entering deleteUserById: userId = {}", userId);

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "User with id=" + userId + " was not found");
        }

        userRepository.deleteById(userId);
        log.info("Exiting deleteUserById");
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsersInfo(GetUsersInfoDto dto) {
        log.info("Entering getUsersInfo: GetUsersInfoDto = {}", dto);
        List<UserEntity> userEntities;
        Pageable pageable = PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize());

        if (dto.getIds() != null) {
            userEntities = userRepository.findAllByIdIn(dto.getIds(), pageable);
        } else {
            userEntities = userRepository.findAll(pageable).toList();
        }

        log.info("Exiting getUsersInfo");

        return mapper.map(userEntities, new TypeToken<List<UserDto>>() {}.getType());
    }
}
