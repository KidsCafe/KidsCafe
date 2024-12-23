package com.sparta.kidscafe.common.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverApiService {

  private static final Logger logger = LoggerFactory.getLogger(NaverApiService.class);

  private final String CLIENT_ID = "zamhzkrtbo";
  private final String CLIENT_SECRET = "4NV9jqLplbSBjSuoZc9AwI4sY6Df5w5feUaa5d7f";

  private final RestTemplate restTemplate;

  public NaverApiService() {
    this.restTemplate = new RestTemplate();
  }

  public Map<String, Object> getCoordinates(String address) {
    String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
        .queryParam("query", address);

    return executeApiRequest(uriBuilder.toUriString(), "Geocoding API");
  }

  public Map<String, Object> searchLocal(String keyword) {
    String apiUrl = "https://naveropenapi.apigw.ntruss.com/v1/search/local.json"; // 수정된 URL
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
        .queryParam("query", keyword)
        .queryParam("display", 10)
        .queryParam("start", 1)
        .queryParam("sort", "sim"); // 유사도순 정렬

    return executeApiRequest(uriBuilder.toUriString(), "Local Search API");
  }

  private Map<String, Object> executeApiRequest(String url, String apiName) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        logger.info("{} 호출 성공: {}", apiName, url);
        return response.getBody();
      } else {
        logger.error("{} 호출 실패: {} (HTTP 상태 코드: {})", apiName, url, response.getStatusCode());
        throw new RuntimeException(apiName + " 호출 실패: HTTP 상태 코드 " + response.getStatusCode());
      }
    } catch (Exception e) {
      logger.error("{} 호출 중 오류 발생: {}", apiName, e.getMessage(), e);
      throw new RuntimeException(apiName + " 호출 중 오류: " + e.getMessage());
    }
  }

  private HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
    headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
