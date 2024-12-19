package com.sparta.kidscafe.domain.lesson.repository;


import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
  List<Lesson> findAllByCafeId(Long cafeId);
}
