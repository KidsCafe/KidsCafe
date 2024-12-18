package com.sparta.kidscafe.domain.review.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReviewCreateRequestDto(
    @Min(0)
    @Max(5)
    double star,
    @NotNull
    String content,
    Long parentId,
    List<Long>imageId
) {
}