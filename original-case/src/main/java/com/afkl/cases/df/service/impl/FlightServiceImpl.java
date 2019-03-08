package com.afkl.cases.df.service.impl;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.afkl.cases.df.model.FlightFareResponse;
import com.afkl.cases.df.service.AuthorizationService;
import com.afkl.cases.df.service.FlightService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Configuration
@Slf4j
public class FlightServiceImpl implements FlightService {

	@Value("${service.airports.url}")
	private String airportsURL;
	@Value("${service.airport.specific.url}")
	private String airportSpecificURL;
	Map<String, Integer> statusMap = new HashMap<>();
	private final AuthorizationService authorizationService;

	@Autowired
	public FlightServiceImpl(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;

	}

	@Override
	public ResponseEntity<String> getAllAirportDetails() {
		return getMockServiceResponse(airportsURL, null);

	}

	private ResponseEntity<String> getMockServiceResponse(String url, String key) {

		ResponseEntity<String> response = null;
		try {
			String accessTokenAsString = authorizationService.getAccessToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + accessTokenAsString);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class, key);
		} catch (Exception ex) {
			log.error("Exception in getting getMockServiceResponse " + ex.getMessage());
		}
		return response;

	}

	@Override
	public ResponseEntity<String> getSelectedAirportDetails(String key) {
		return getMockServiceResponse(airportSpecificURL, key);
	}

	@Override
	public FlightFareResponse getFlightFareDetails(String origin, String dest, String currency) {
		Map<String, Object> originMap = new HashMap<>();
		Map<String, Object> destMap = new HashMap<>();

		FlightFareResponse flightFareResponse = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode input = mapper.readTree(getAllAirportDetails().getBody());
			JsonNode array = input.get("_embedded").get("locations");

			array.forEach(element -> {
				JsonNode code = element.get("code");
				if (code.asText().equalsIgnoreCase(origin)) {
					originMap.put("originDetails", element);
				}

				if (code.asText().equalsIgnoreCase(dest)) {
					destMap.put("destDetails", element);
				}
			});
			Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 6000));
			BigDecimal fare = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(100, 3500)).setScale(2,
					HALF_UP);
			flightFareResponse = FlightFareResponse.builder().amount(fare.doubleValue()).origin(originMap)
					.destination(destMap).currency(currency.toUpperCase()).build();
		}

		catch (Exception ex) {
			log.error("Exception in getting getFlightFareDetails " + ex.getMessage());
		}
		return flightFareResponse;

	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
