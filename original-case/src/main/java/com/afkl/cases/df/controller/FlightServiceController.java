package com.afkl.cases.df.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.afkl.cases.df.model.FlightServiceResponse;
import com.afkl.cases.df.model.FinalFlightServiceResponse;
import com.afkl.cases.df.model.FlightFareResponse;
import com.afkl.cases.df.service.FlightService;
import com.afkl.cases.df.service.FlightServiceStatistics;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/travelService")
public class FlightServiceController {

	private final FlightService flightService;
	private final FlightServiceStatistics flightServiceStatistics;

	@Autowired
	public FlightServiceController(FlightService flightService, FlightServiceStatistics flightServiceStatistics) {
		this.flightService = flightService;
		this.flightServiceStatistics = flightServiceStatistics;
	}

	@GetMapping(value = "/airports", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> getAllAirportDetails() throws InterruptedException, ExecutionException {

		String response = flightService.getAllAirportDetails().get();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/airports/{key}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> getAirportDetails(@PathVariable("key") String key)
			throws InterruptedException, ExecutionException {

		String response = flightService.getSelectedAirportDetails(key).get();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/fares/{origin}/{dest}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FlightFareResponse> getFareDetails(@PathVariable("origin") String origin,
			@PathVariable("dest") String dest, @RequestParam(value = "currency", defaultValue = "EUR") String currency)
			throws InterruptedException, ExecutionException {
		FlightFareResponse response = flightService.getFlightFareDetails(origin, dest, currency).get();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/calculateStatistics", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FlightServiceResponse> calculateStatistics() {
		FlightServiceResponse response = flightServiceStatistics.calculateStatistics();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/runAsynch", produces = { MediaType.APPLICATION_JSON_VALUE })
	public FinalFlightServiceResponse testAsynch(@RequestParam String key, @RequestParam String origin,
			@RequestParam String dest, @RequestParam(value = "currency", defaultValue = "EUR") String currency)
			throws InterruptedException, ExecutionException, IOException {
		CompletableFuture<String> allAirportDetails = flightService.getAllAirportDetails();
		CompletableFuture<String> airportDetails = flightService.getSelectedAirportDetails(key);
		CompletableFuture<FlightFareResponse> flightFare = flightService.getFlightFareDetails(origin, dest, currency);
		CompletableFuture.allOf(allAirportDetails, airportDetails, flightFare).join();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode allAirPorts = mapper.readTree(allAirportDetails.get());
		JsonNode airPort = mapper.readTree(airportDetails.get());

		return FinalFlightServiceResponse.builder().allAirportDetails(allAirPorts).selectedAirportDetails(airPort)
				.flightFareDetails(flightFare.get()).build();
	}

}
