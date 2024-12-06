package com.sparta.kidscafe.domain.cafe.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.fee.entity.Fee;
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
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Column(nullable = false)
  private int size;

  @Column(nullable = false)
  private boolean multiFamily;

  @Column(nullable = false)
  private boolean roomExist;

  @Column
  private String dayOff;

  @Column(nullable = false)
  private boolean parking;

  @Column(nullable = false)
  private boolean restaurant;

  @Column
  private String hyperlink;

  @Column(updatable = false)
  private LocalTime openedAt;

  @Column(updatable = false)
  private LocalTime closedAt;

  @Builder.Default
  @OneToMany(mappedBy = "cafe", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Bookmark> bookmarks = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "cafe", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<CafeImage> cafeImages = new ArrayList<>();

  // TODO khj 한방 쿼리가 된다면 밑에 항목들 제거
  @Builder.Default
  @OneToMany(mappedBy = "cafe", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Room> rooms = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "cafe", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Fee> fees = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "cafe", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<PricePolicy> pricePolicies = new ArrayList<>();
}
