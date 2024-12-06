package com.sparta.kidscafe.domain.review.dto.request;


import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequestDto(
    @NotNull
    double star,
    @NotNull
    String content
) {
}