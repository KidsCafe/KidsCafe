package com.sparta.kidscafe.common.enums;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LoginType {
  BASIC("BASIC"),
  GITHUB("GITHUB"),
  NAVER("NAVER");

  private final String LoginTypeValue;

  public String getValue(){
    return LoginTypeValue;
  }

  @JsonCreator
  public static LoginType parsing(String inputValue){
    return Stream.of(LoginType.values())
        .filter(category -> category.toString().equals(inputValue.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid LoginType: " + inputValue));
  }
}
