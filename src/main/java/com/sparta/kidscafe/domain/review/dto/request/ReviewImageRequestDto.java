package com.sparta.kidscafe.domain.review.dto.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ReviewImageRequestDto (
    List<MultipartFile> reviewImages
){
}
