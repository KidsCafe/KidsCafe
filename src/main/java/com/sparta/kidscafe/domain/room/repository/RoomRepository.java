package com.sparta.kidscafe.domain.room.repository;

import com.sparta.kidscafe.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
