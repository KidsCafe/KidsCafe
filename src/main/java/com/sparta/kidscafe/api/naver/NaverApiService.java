package com.sparta.kidscafe.api.naver;

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

  public NaverApiResponse searchLocal(String keyword) {
    RequestEntity<Void> requestEntity = RequestEntity
        .get(makeUrl(keyword))
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

  private URI makeUrl(String keyword) {
    return UriComponentsBuilder
        .fromUriString("https://openapi.naver.com")
        .path("/v1/search/local.json")
        .queryParam("query", keyword)
        .queryParam("display", 5)
        .queryParam("start", 1)
        .queryParam("sort", "random")
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