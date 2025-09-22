package com.ekvitou.studentmanagement.model.dto;

import lombok.Builder;

@Builder
public record StudentUpdateDto(
        String name,
        Integer age,
        String email,
        String grade,
        Integer score
){}
