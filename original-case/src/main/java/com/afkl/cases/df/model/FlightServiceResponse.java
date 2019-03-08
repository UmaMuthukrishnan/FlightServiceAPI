package com.afkl.cases.df.model;

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
public class FlightServiceResponse {

	private int totalRequest;
	private int successRequest;
	private int notFoundRequest;
	private int serverErrorRequest;
	private int minResponseTime;
	private int maxResponseTime;
	private double averageResponseTime;
}
