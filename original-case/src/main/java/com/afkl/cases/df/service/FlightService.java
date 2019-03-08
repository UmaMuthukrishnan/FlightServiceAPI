package com.afkl.cases.df.service;

import java.util.concurrent.CompletableFuture;
import com.afkl.cases.df.model.FlightFareResponse;

public interface FlightService {
	
	CompletableFuture<String> getAllAirportDetails();

	CompletableFuture<String> getSelectedAirportDetails(String key);

	CompletableFuture<FlightFareResponse> getFlightFareDetails(String origin, String dest, String currency);

}
