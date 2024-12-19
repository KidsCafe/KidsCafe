package com.sparta.kidscafe.domain.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.domain.lesson.service.LessonService;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyLesson;
import com.sparta.kidscafe.dummy.DummyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

public class LessonServiceTest {
  @InjectMocks
  private LessonService lessonService;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private LessonCreateRequestDto createRequestDto;

  @Mock
  private AuthValidationCheck authValidationCheck;

  @Mock
  private CafeValidationCheck cafeValidationCheck;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser(RoleType role) {
    return new AuthUser(1L, "hong@email.com", role);
  }

  @Test
  @DisplayName("활동 클래스 등록 성공 - 사장님")
  void createLesson_Success() {
    // given
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    Lesson lesson = DummyLesson.createDummyLesson(cafe);

    when(createRequestDto.convertDtoToEntity(cafe)).thenReturn(lesson);
    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);
    when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

    // when
    StatusDto result = lessonService.createLesson(authUser, cafeId, createRequestDto);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    verify(lessonRepository).save(any(Lesson.class));
  }
}
