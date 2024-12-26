package com.sparta.kidscafe.api.naver.service;

import com.sparta.kidscafe.api.naver.response.NaverApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverApiService {

  private static final Logger logger = LoggerFactory.getLogger(NaverApiService.class);

  private final String CLIENT_ID = "Q4gqBKSldsxAE2XUkfC7";
  private final String CLIENT_SECRET = "M3swtmyD46";

  private final RestTemplate restTemplate;

  public NaverApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // 로컬 검색 API 호출
  public NaverApiResponse searchLocal(String keyword, int display, int start, String sort) {
    String apiUrl = "https://openapi.naver.com/v1/search/local.json";
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
        .queryParam("query", keyword)
        .queryParam("display", display)
        .queryParam("start", start)
        .queryParam("sort", sort);

    return executeApiRequest(uriBuilder.toUriString(), "Local Search API");
  }

  // API 요청 실행 및 결과 처리
  private NaverApiResponse executeApiRequest(String url, String apiName) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    try {
      ResponseEntity<NaverApiResponse> response = restTemplate.exchange(
          url, HttpMethod.GET, requestEntity, NaverApiResponse.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        logger.info("[{}] 호출 성공: {}", apiName, url);
        return response.getBody();
      } else {
        logger.error("[{}] 호출 실패: {} (HTTP 상태 코드: {})", apiName, url, response.getStatusCode());
        throw new RuntimeException(apiName + " 호출 실패: HTTP 상태 코드 " + response.getStatusCode());
      }
    } catch (HttpClientErrorException e) {
      logger.error("[{}] 호출 중 오류 발생: {}, 상세: {}", apiName, e.getResponseBodyAsString(),
          e.getMessage(), e);
      throw new RuntimeException(apiName + " 호출 중 오류: " + e.getResponseBodyAsString());
    }
  }

  // HTTP 요청 헤더 생성
  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", CLIENT_ID);
    headers.set("X-Naver-Client-Secret", CLIENT_SECRET);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}