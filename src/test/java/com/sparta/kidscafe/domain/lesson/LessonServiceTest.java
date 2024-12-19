package com.sparta.kidscafe.domain.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.dto.response.LessonResponseDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.domain.lesson.service.LessonService;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyLesson;
import com.sparta.kidscafe.dummy.DummyUser;
import java.util.List;
import java.util.Optional;
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
  private LessonRepository lessonRepository;

  @Mock
  private LessonCreateRequestDto createRequestDto;

  @Mock
  private CafeValidationCheck cafeValidationCheck;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser() {
    return new AuthUser(1L, "hong@email.com", RoleType.OWNER);
  }

  private LessonCreateRequestDto createRequestDto() {
    LessonCreateRequestDto requestDto = new LessonCreateRequestDto();
    requestDto.setName("Test-Lesson");
    return requestDto;
  }

  @Test
  @DisplayName("활동 클래스 등록 성공 - 사장님")
  void createLesson_Success() {
    // given
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    Lesson lesson = DummyLesson.createDummyLesson(cafe);
    LessonCreateRequestDto requestDto = createRequestDto();

    when(createRequestDto.convertDtoToEntity(cafe)).thenReturn(lesson);
    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);
    when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

    // when
    StatusDto result = lessonService.createLesson(authUser, cafeId, requestDto);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("[" + requestDto.getName() + "] 생성 성공", result.getMessage());
    verify(lessonRepository).save(any(Lesson.class));
  }

  @Test
  @DisplayName("활동 클래스 조회 성공 - 사장님")
  void searchLesson_Success() {
    // given
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    List<Lesson> lessons = DummyLesson.createDummyLessons(cafe, 5);
    List<LessonResponseDto> responseLessons = lessons.stream().map(LessonResponseDto::from).toList();

    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);
    when(lessonRepository.findAllByCafeId(cafeId)).thenReturn(lessons);

    // when
    ListResponseDto<LessonResponseDto> result = lessonService.searchLesson(authUser, cafeId);

    // then
    verify(cafeValidationCheck).validMyCafe(cafeId, authUser.getId());
    verify(lessonRepository).findAllByCafeId(cafeId);

    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("활동 클래스 조회 성공", result.getMessage());
    assertEquals(responseLessons.size(), result.getData().size());
  }

  @Test
  @DisplayName("활동 클래스 수정 성공 - 사장님")
  void updateLesson_Success() {
    // given
    Long lessonId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, null);
    Lesson lesson = DummyLesson.createDummyLesson(cafe, lessonId);
    LessonCreateRequestDto requestDto = createRequestDto();
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

    // when
    StatusDto result = lessonService.updateLesson(lessonId, requestDto);

    // then
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("[" + requestDto.getName() + "] 수정 성공", result.getMessage());
  }

  @Test
  @DisplayName("활동 클래스 삭제 성공 - 사장님")
  void deleteLesson_Success() {
    // given
    Long lessonId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, null);
    Lesson lesson = DummyLesson.createDummyLesson(cafe, lessonId);
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

    // when
    lessonService.deleteLesson(lessonId);

    // then
    verify(lessonRepository).findById(lessonId);
    verify(lessonRepository).delete(lesson);
  }
}
