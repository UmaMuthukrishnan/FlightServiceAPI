package com.afkl.cases.df.service;

import org.springframework.http.ResponseEntity;

import com.afkl.cases.df.model.FlightFareResponse;

public interface FlightService {
	
	ResponseEntity<String> getAllAirportDetails();

	ResponseEntity<String> getSelectedAirportDetails(String key);

	FlightFareResponse getFlightFareDetails(String origin, String dest, String currency);

}
