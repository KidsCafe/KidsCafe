package com.sparta.kidscafe.domain.cafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kidscafe.domain.cafe.dto.response.NaverCafeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverMapService {

    private static final String NAVER_LOCAL_SEARCH_URL =
            "https://openapi.naver.com/v1/search/local.json?query={query}&display=5";
    private static final String CLIENT_ID = "zamhzkrtbo"; // Naver Client ID
    private static final String CLIENT_SECRET = "4NV9jqLplbSBjSuoZc9AwI4sY6Df5w5feUaa5d7f"; // Naver Client Secret

    private final RestTemplate restTemplate;

    public List<NaverCafeResponseDto> searchCafes(String query, String region) {
        // 1. HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 2. API 요청 URL 구성
        String searchQuery = query + " " + region;

        // 3. API 호출 및 응답 처리
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_LOCAL_SEARCH_URL,
                    HttpMethod.GET,
                    entity,
                    String.class,
                    searchQuery
            );

            // 4. JSON 응답을 DTO 리스트로 변환
            return parseResponse(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("네이버 API 호출 중 오류가 발생했습니다.", e);
        }
    }

    private List<NaverCafeResponseDto> parseResponse(String jsonResponse) {
        try {
            // Jackson ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // 'items' 배열 추출 및 변환
            JsonNode itemsNode = rootNode.path("items");
            return objectMapper.convertValue(itemsNode, new TypeReference<List<NaverCafeResponseDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류가 발생했습니다.", e);
        }
    }
}