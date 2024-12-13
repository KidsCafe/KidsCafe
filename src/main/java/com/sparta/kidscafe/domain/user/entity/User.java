package com.sparta.kidscafe.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.kidscafe.api.oauth2.controller.dto.OAuth2UserProfileDto;
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

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false, length = 30)
  private String nickname;

  @Column(nullable = false)
  private String address;

  @Enumerated(value = EnumType.STRING)
  private RoleType role;

  @Enumerated(value = EnumType.STRING)
  private LoginType loginType;

  @Column
  private String oauthId;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Bookmark> bookmarks = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Review> reviews = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Reservation> reservations = new ArrayList<>();

  public User (Long id, String email, RoleType role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public User update(String email, String name) {
    this.name = name;
    this.email = email;
    return this;
  }

  public User(Long id, String oauthId, String name, String email, RoleType roleType){
    this.id = id;
    this.oauthId = oauthId;
    this.name = name;
    this.email = email;
    this.role = roleType;
  }
}
