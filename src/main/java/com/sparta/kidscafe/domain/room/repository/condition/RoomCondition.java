package com.sparta.kidscafe.domain.room.repository.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import org.springframework.stereotype.Component;

@Component
public class RoomCondition {

  private final QRoom room = QRoom.room;

  public BooleanExpression eqId(Long id) {
    if (id == null)
      return null;

    return room.id.eq(id);
  }

  public BooleanExpression selectExistRoom() {
    return new CaseBuilder()
        .when(room.cafe.id.countDistinct().goe(1))
        .then(true)
        .otherwise(false);
  }

  public BooleanExpression existRoom(Boolean existRoom) {
    if (existRoom == null || !existRoom) {
      return null;
    }

    return room.cafe.id.countDistinct().goe(1);
  }
}
