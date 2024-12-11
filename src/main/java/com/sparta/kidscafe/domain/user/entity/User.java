package com.sparta.kidscafe.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.common.enums.LoginType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.review.entity.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false, length = 30)
  private String nickname;

  @Column(nullable = false)
  private String address;

  @Column
  private String imagePath;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private RoleType role;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private LoginType loginType;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  private List<Reservation> reservations = new ArrayList<>();

  public User (Long id, String email, RoleType role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public void updateProfile(String name, String nickname, String address) {
    if (name != null) {
      this.name = name;
    }
    if (nickname != null) {
      this.nickname = nickname;
    }
    if (address != null) {
      this.address = address;
    }
  }
}
