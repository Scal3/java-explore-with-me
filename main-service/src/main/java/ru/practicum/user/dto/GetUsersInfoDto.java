package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetUsersInfoDto {

    private List<Integer> ids;

    private int from;

    private int size;
}
