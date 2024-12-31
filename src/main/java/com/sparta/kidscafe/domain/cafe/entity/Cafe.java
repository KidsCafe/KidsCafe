package com.sparta.kidscafe.domain.cafe.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cafe")
public class Cafe extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String region;

  @Column(nullable = false)
  private String address;

  @Column
  private Point location;

  @Column(nullable = false)
  private int size;

  @Column(nullable = false)
  private boolean multiFamily;

  @Column
  private String dayOff;

  @Column(nullable = false)
  private boolean parking;

  @Column(nullable = false)
  private boolean restaurant;

  @Column(nullable = false)
  private boolean careService;

  @Column(nullable = false)
  private boolean swimmingPool;

  @Column(nullable = false)
  private boolean clothesRental;

  @Column(nullable = false)
  private boolean monitoring;

  @Column(nullable = false)
  private boolean feedingRoom;

  @Column(nullable = false)
  private boolean outdoorPlayground;

  @Column(nullable = false)
  private boolean safetyGuard;

  @Column
  private String hyperlink;

  @Column(updatable = false)
  private LocalTime openedAt;

  @Column(updatable = false)
  private LocalTime closedAt;

  @OneToOne(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
  CafeViewCount cafeViewCount;

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Room> rooms = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Fee> fees = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<PricePolicy> pricePolicies = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Lesson> lessons = new ArrayList<>();

  public void update(CafeSimpleRequestDto requestDto, Point location) {
    name = requestDto.getName();
    region = requestDto.getRegion();
    address = requestDto.getAddress();
    size = requestDto.getSize();
    multiFamily = requestDto.isMultiFamily();
    dayOff = requestDto.getDayOff();
    parking = requestDto.isParking();
    hyperlink = requestDto.getHyperLink();
    openedAt = requestDto.getOpenedAt();
    closedAt = requestDto.getClosedAt();
    this.location = location;
  }

  public void initRooms(List<Room> rooms) {
    this.rooms = rooms;
  }

  public void initFees(List<Fee> fees) {
    this.fees = fees;
  }

  public void initLessons(List<Lesson> lessons) {
    this.lessons = lessons;
  }

  public void initPricePolicies(List<PricePolicy> pricePolicies) {
    this.pricePolicies = pricePolicies;
  }
}
