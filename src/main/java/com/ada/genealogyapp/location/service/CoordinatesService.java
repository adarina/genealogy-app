package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.GeoCoordinates;
import com.ada.genealogyapp.location.dto.NominatimResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static java.lang.Double.parseDouble;
import static java.util.Objects.nonNull;
import static org.springframework.web.util.UriUtils.encode;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final RestTemplate restTemplate;

    public GeoCoordinates getCoordinates(String locationName, String state) {

        String encodedLocation = encode(locationName, StandardCharsets.UTF_8);
        String encodedState = nonNull(state) ? encode(state, StandardCharsets.UTF_8) : "";

        String url = nonNull(state) ?
                String.format("https://nominatim.openstreetmap.org/search?q=%s,%s&format=json&limit=1", encodedLocation, encodedState) :
                String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1", encodedLocation);

        ResponseEntity<NominatimResponse[]> response = restTemplate.getForEntity(url, NominatimResponse[].class);

        if (nonNull(response.getBody()) && response.getBody().length > 0) {
            NominatimResponse nominatimResponse = response.getBody()[0];
            return new GeoCoordinates(parseDouble(nominatimResponse.getLat()), parseDouble(nominatimResponse.getLon()));
        }
        throw new RuntimeException("Location not found: " + locationName);
    }
}
