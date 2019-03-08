package com.afkl.cases.df.controller;

import java.util.Optional;

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
import com.afkl.cases.df.model.FlightFareResponse;
import com.afkl.cases.df.service.FlightService;
import com.afkl.cases.df.service.FlightServiceStatistics;

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
	public ResponseEntity<String> getAllAirportDetails() {

		String response = flightService.getAllAirportDetails().getBody();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/airports/{key}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> getAirportDetails(@PathVariable("key") String key) {

		String response = flightService.getSelectedAirportDetails(key).getBody();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/fares/{origin}/{dest}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FlightFareResponse> getFareDetails(@PathVariable("origin") String origin,
			@PathVariable("dest") String dest,
			@RequestParam(value = "currency", defaultValue = "EUR") String currency) {
		FlightFareResponse response = flightService.getFlightFareDetails(origin, dest, currency);
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/calculateStatistics", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FlightServiceResponse> calculateStatistics() {
		FlightServiceResponse response = flightServiceStatistics.calculateStatistics();
		return Optional.of(response).map(u -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
