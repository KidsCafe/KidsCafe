package com.sparta.kidscafe.domain.cafe.repository.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.sparta.kidscafe.domain.room.entity.QRoom;
import org.springframework.stereotype.Component;

@Component
public class RoomCondition {

  private final QRoom room = QRoom.room;

  public BooleanExpression existRoom() {
    return new CaseBuilder()
        .when(room.cafe.id.countDistinct().goe(1))
        .then(true)
        .otherwise(false);
  }

  public BooleanExpression existRoom(boolean existRoom) {
    if (!existRoom) {
      return null;
    }

    return room.cafe.id.countDistinct().goe(1);
  }
}
