package com.sparta.kidscafe.domain.lesson.repository;


import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
  List<Lesson> findAllByCafeId(Long cafeId);
}
