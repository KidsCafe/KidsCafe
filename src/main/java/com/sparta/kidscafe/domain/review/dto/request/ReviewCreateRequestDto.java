package com.sparta.kidscafe.domain.review.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import java.util.List;

public record ReviewCreateRequestDto(
    @Min(0)
    @Max(5)
    double star,
    @NotNull
    String content,
    Long parentId,
    @Size(max = 5, message = "이미지는 최대 5개까지만 첨부할 수 있습니다.")
    List<Long>imageId
) {
}