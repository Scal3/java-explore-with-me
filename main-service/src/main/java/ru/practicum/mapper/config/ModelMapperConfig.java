package ru.practicum.mapper.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.entity.RequestEntity;
import ru.practicum.user.entity.UserEntity;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        TypeMap<RequestEntity, ParticipationRequestDto> requestEntityParticipationRequestDtoTypeMap =
                mapper.createTypeMap(RequestEntity.class, ParticipationRequestDto.class);

        Converter<UserEntity, Long> userEntityLongConverter = context -> {
            if (context.getSource() == null) return null;

            return context.getSource().getId();
        };

        Converter<EventEntity, Long> eventEntityLongConverter = context -> {
            if (context.getSource() == null) return null;

            return context.getSource().getId();
        };

        requestEntityParticipationRequestDtoTypeMap.addMappings(
                m -> {
                    m.using(userEntityLongConverter)
                            .map(RequestEntity::getRequester, ParticipationRequestDto::setRequester);
                    m.using(eventEntityLongConverter)
                            .map(RequestEntity::getEvent, ParticipationRequestDto::setEvent);
                }
        );

        return mapper;
    }
}
