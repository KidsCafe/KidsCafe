package com.sparta.kidscafe.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class GeoUtil {
  private final GeometryFactory geometryFactory = new GeometryFactory();

  public Point convertGeoToPoint(Double lon, Double lat) {
    Point location = geometryFactory.createPoint(new Coordinate(lon, lat));
    location.setSRID(4326);
    return location;
  }

  public boolean validWktPoint(Double lon, Double lat) {
    // 경도 범위
    if(lon == null || lon < -90 || lon > 90) {
      return false;
    }

    // 위도 범위
    return lat != null && lat >= -180 && lat <= 180;
  }
}
