package com.afkl.cases.df.model;


import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinalFlightServiceResponse {
	private JsonNode allAirportDetails;
	private JsonNode selectedAirportDetails;
	private FlightFareResponse flightFareDetails;

}
