package com.afkl.cases.df.service.impl;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
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
	private final RestTemplate restTemplate;

	@Autowired
	public FlightServiceImpl(AuthorizationService authorizationService, RestTemplateBuilder restTemplateBuilder) {
		this.authorizationService = authorizationService;
		this.restTemplate = restTemplateBuilder.build();

	}

	@Override
	@Async
	public CompletableFuture<String> getAllAirportDetails() throws InterruptedException {
		log.info("getAllAirportDetails :: START {} ", LocalDateTime.now());
		return getMockServiceResponse(airportsURL, null);

	}

	@Async("asyncExecutor")
	private CompletableFuture<String> getMockServiceResponse(String url, String key) throws InterruptedException {

		log.info("getMockServiceResponse :: START {} ", LocalDateTime.now());
		long startTime = System.currentTimeMillis();
		String response = null;
		String accessTokenAsString = authorizationService.getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + accessTokenAsString);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, key).getBody();
		Thread.sleep(1000L);
		long endTime = System.currentTimeMillis();
		double time = (endTime - startTime) / 1000.000;
		log.info("getMockServiceResponse :: END time: {} seconds", time);
		return CompletableFuture.completedFuture(response);

	}

	@Override
	@Async("asyncExecutor")
	public CompletableFuture<String> getSelectedAirportDetails(String key) throws InterruptedException {
		log.info("getSelectedAirportDetails :: START {} ", LocalDateTime.now());
		return getMockServiceResponse(airportSpecificURL, key);
	}

	@Override
	@Async("asyncExecutor")
	public CompletableFuture<FlightFareResponse> getFlightFareDetails(String origin, String dest, String currency)
			throws Exception {
		log.info("getFlightFareDetails :: START {} ", LocalDateTime.now());
		long startTime = System.currentTimeMillis();
		Map<String, Object> originMap = new HashMap<>();
		Map<String, Object> destMap = new HashMap<>();

		FlightFareResponse flightFareResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode input = mapper.readTree(getAllAirportDetails().get());
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
		BigDecimal fare = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(100, 3500)).setScale(2, HALF_UP);
		flightFareResponse = FlightFareResponse.builder().amount(fare.doubleValue()).origin(originMap)
				.destination(destMap).currency(currency.toUpperCase()).build();
		Thread.sleep(1000L);
		long endTime = System.currentTimeMillis();
		double time = (endTime - startTime) / 1000.000;
		log.info("getFlightFareDetails :: END time: {} seconds", time);
		return CompletableFuture.completedFuture(flightFareResponse);

	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
