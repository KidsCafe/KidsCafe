package com.sparta.kidscafe.domain.pricepolicy.dto.request;

import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 추가된 부분
@Builder
public class PricePolicyCreateRequestDto {

    @NotNull(message = "TargetType은 필수 입력값입니다.")
    private TargetType targetType; // 정책 대상 종류 (FEE, ROOM, PEOPLE 등)

    @NotNull(message = "Target ID는 필수 입력값입니다.")
    @Positive(message = "Target ID는 양수만 허용됩니다.")
    private Long targetId; // 적용 대상 ID

    @NotBlank(message = "Title은 필수 입력값입니다.")
    @Size(max = 50, message = "Title은 최대 50자까지 입력 가능합니다.")
    private String title; // 정책 이름

    @NotBlank(message = "DayType은 필수 입력값입니다.")
    @Pattern(regexp = "^(월|화|수|목|금|토|일)(,\\s*(월|화|수|목|금|토|일))*$",
            message = "DayType은 요일을 쉼표로 구분하여 입력해야 합니다. (예: '월, 화, 수')")
    private String dayType; // 적용 요일 (ex: "월, 화, 수")

    @NotNull(message = "Rate는 필수 입력값입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rate는 0보다 커야 합니다.")
    @DecimalMax(value = "50.0", message = "Rate는 최대 50.0까지 입력 가능합니다.")
    @Digits(integer = 2, fraction = 2, message = "Rate는 정수 2자리 이하, 소수점 2자리 이하만 허용됩니다.")
    private Double rate; // 요금 비율 (ex: 1.2배)


    public PricePolicy convertDtoToEntity(Cafe cafe) {
        return PricePolicy.builder()
                .cafe(cafe)
                .targetType(targetType)
                .targetId(targetId)
                .title(title)
                .dayType(dayType)
                .rate(rate)
                .build();
    }
}
