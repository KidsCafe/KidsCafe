package com.sparta.kidscafe.api.naver.service;

import com.sparta.kidscafe.api.naver.response.NaverApiResponse;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j(topic = "naver 크롤링:")
public class NaverApiService {

  @Value("${naver.api.client-id}")
  private String clientId;
  @Value("${naver.api.client-secret}")
  private String clientSecret;
  private final RestTemplate restTemplate;

  public NaverApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public NaverApiResponse searchLocal(String keyword, int display, int start, String sort) {
    RequestEntity<Void> requestEntity = RequestEntity
        .get(makeUrl(keyword, display, start, sort))
        .headers(makeHeaders())
        .build();

    ResponseEntity<NaverApiResponse> responseEntity = restTemplate.exchange(
        requestEntity,
        NaverApiResponse.class
    );

    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      log.info("[{}] 호출 성공", keyword);
      return responseEntity.getBody();
    } else {
      log.error(
          "[{}] 호출 실패: (HTTP 상태 코드: {})",
          keyword,
          responseEntity.getStatusCode()
      );

      String errorMsg = keyword + " 호출 실패: HTTP 상태 코드 " + responseEntity.getStatusCode();
      throw new BusinessException(ErrorCode.BAD_REQUEST, errorMsg);
    }
  }

  private URI makeUrl(String keyword, int display, int start, String sort) {
    return UriComponentsBuilder
        .fromUriString("https://openapi.naver.com")
        .path("/v1/search/local.json")
        .queryParam("query", keyword)
        .queryParam("display", display)
        .queryParam("start", start)
        .queryParam("sort", sort)
        .encode()
        .build()
        .toUri();
  }

  private HttpHeaders makeHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", clientId);
    headers.set("X-Naver-Client-Secret", clientSecret);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}