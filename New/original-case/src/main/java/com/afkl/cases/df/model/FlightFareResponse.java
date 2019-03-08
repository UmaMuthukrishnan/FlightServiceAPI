package com.afkl.cases.df.model;

import java.util.Map;

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
public class FlightFareResponse {

	private double amount;
	private Map<String, Object> origin;
	private Map<String, Object> destination;
	private String currency;
}
