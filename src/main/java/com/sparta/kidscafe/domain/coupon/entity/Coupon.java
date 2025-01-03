package com.sparta.kidscafe.domain.coupon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id")
	private Cafe cafe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String discountType;

	@Column(nullable = true)
	private Double discountRate;

	@Column(nullable = true)
	private Long discountPrice;

	@Column(nullable = false)
	private boolean isUsed;

	@Column(nullable = false)
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validTo;

	public void update(String name, String discountType, Double discountRate, Long discountPrice, LocalDate validTo) {
		this.name = name;
		this.discountType = discountType;
		this.discountRate = discountRate;
		this.discountPrice = discountPrice;
		this.validTo = validTo;
	}

	public void assignToUser(User user){
		this.user = user;
	}

	public void markAsUsed(){
		this.isUsed = true;
	}
}
