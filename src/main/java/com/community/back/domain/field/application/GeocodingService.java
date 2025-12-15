package com.community.back.domain.field.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.maps.api.key:}")
    private String googleMapsApiKey;

    public static class Coordinates {
        public final Double lat;
        public final Double lng;

        public Coordinates(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    /**
     * 주소를 위경도로 변환 (Google Maps Geocoding API 사용)
     */
    public Coordinates getCoordinatesFromAddress(String address) {
        // Google Maps API 키가 없으면 기본값 반환
        if (googleMapsApiKey == null || googleMapsApiKey.isEmpty()) {
            log.warn("Google Maps API key not configured. Using default coordinates (Seoul City Hall)");
            return new Coordinates(37.5665, 126.9780);
        }

        try {

            // UriComponentsBuilder를 사용해서 올바른 URL 인코딩
            String url = UriComponentsBuilder
                .fromUriString("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", address)
                .queryParam("key", googleMapsApiKey)
                .queryParam("language", "ko")
                .queryParam("region", "kr")
                .build()
                .toUriString();

            log.info("Google Geocoding API Request URL: {}", url);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            log.info("Google Geocoding API Response: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            String status = root.get("status").asText();

            if ("OK".equals(status)) {
                JsonNode results = root.get("results");
                if (results != null && results.isArray() && results.size() > 0) {
                    JsonNode firstResult = results.get(0);
                    String formattedAddress = firstResult.get("formatted_address").asText();
                    JsonNode location = firstResult.get("geometry").get("location");
                    Double lat = location.get("lat").asDouble();
                    Double lng = location.get("lng").asDouble();

                    log.info("Geocoding success for address '{}': formatted='{}', lat={}, lng={}", address, formattedAddress, lat, lng);
                    return new Coordinates(lat, lng);
                }
            } else {
                log.warn("Geocoding API returned status: {} for address: {}", status, address);
            }

            log.warn("No geocoding results found for address: {}", address);
            return new Coordinates(37.5665, 126.9780); // 기본값

        } catch (Exception e) {
            log.error("Geocoding failed for address: {}. Error: {}", address, e.getMessage());
            return new Coordinates(37.5665, 126.9780); // 기본값
        }
    }
}
