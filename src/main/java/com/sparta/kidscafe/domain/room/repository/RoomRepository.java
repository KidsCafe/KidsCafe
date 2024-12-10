package com.sparta.kidscafe.domain.room.repository;

import com.sparta.kidscafe.domain.room.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

  List<Room> findAllByCafeId(Long cafeId);
}
