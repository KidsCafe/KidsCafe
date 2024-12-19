package com.sparta.kidscafe.api.address;

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

  public Point convertAddressToGeo(String address) {
    // HTTP 요청 보내기
    RequestEntity<Void> requestEntity = RequestEntity
        .get(makeUrl(address))
        .headers(makeHeaders())
        .build();
    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

    // HTTP 응답 (JSON) -> 파싱
    return fromJsonToAddress(responseEntity.getBody());
  }

  private URI makeUrl(String address) {
    return UriComponentsBuilder
        .fromUriString("https://dapi.kakao.com")
        .path("/v2/local/search/address.json")
        .queryParam("query", address)
        .encode()
        .build()
        .toUri();
  }

  private HttpHeaders makeHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json;charset=UTF-8");
    headers.add("Authorization", "KakaoAK " + restApiKey);
    return headers;
  }

  private Point fromJsonToAddress(String responseEntity) {
    JSONObject jsonObject = new JSONObject(responseEntity);
    JSONArray documents = jsonObject.getJSONArray("documents");
    JSONObject address = documents.getJSONObject(0);
    Double lon = Double.parseDouble(address.get("x").toString());
    Double lat = Double.parseDouble(address.get("y").toString());
    return geoUtil.convertGeoToPoint(lon, lat);
  }
}
