package com.sparta.kidscafe.api.address;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kidscafe.common.util.GeoUtil;
import java.net.URI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MapService {

  @Value("${kakao.rest-api-key}")
  private String restApiKey;

  private final RestTemplate restTemplate;
  private final GeoUtil geoUtil;

  public MapService(RestTemplateBuilder builder, GeoUtil geoUtil) {
    this.restTemplate = builder.build();
    this.geoUtil = geoUtil;
  }

  public Point convertAddressToGeo(String address) throws JsonProcessingException {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
        .fromUriString("https://dapi.kakao.com")
        .path("/v2/local/search/address")
        .encode()
        .build()
        .toUri();

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    headers.add("Authorization", "KakaoAK "  + restApiKey);

    RequestEntity<Void> requestEntity = RequestEntity
        .get(uri)
        .headers(headers)
        .build();

    // HTTP 요청 보내기
    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

    // HTTP 응답 (JSON) -> 파싱
    JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());
    return fromJsonToAddress(jsonNode);
  }

  private Point fromJsonToAddress(JsonNode responseEntity) {
    JSONArray documents = new JSONArray(responseEntity.get(0));
    JSONObject address = documents.getJSONObject(0);
    Double lon = Double.parseDouble(address.get("x").toString());
    Double lat = Double.parseDouble(address.get("y").toString());
    return geoUtil.convertGeoToPoint(lon, lat);
  }
}
