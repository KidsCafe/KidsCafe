package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafesSimpleCreateRequestDto {

  @Valid
  private List<CafeSimpleRequestDto> cafes;

  public List<Cafe> convertDtoToEntity(User user) {
    List<Cafe> cafes = new ArrayList<>();
    for (CafeSimpleRequestDto cafeDto : this.cafes) {
      Cafe cafe = cafeDto.convertDtoToEntityByCafe(user);
      cafes.add(cafe);
    }
    return cafes;
  }
}
