package com.afkl.cases.df.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.afkl.cases.df.model.FlightServiceResponse;
import com.afkl.cases.df.model.Response;
import com.afkl.cases.df.service.FlightServiceStatistics;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
public class FlightServiceStatisticsImpl implements FlightServiceStatistics {

	@Value("${service.outputFile}")
	private String fileName;

	private static <K> void incrementValue(Map<K, Integer> map, K key) {
		map.merge(key, 1, Integer::sum);
	}
	@Override
	public FlightServiceResponse calculateStatistics() {
		ObjectMapper mapper = new ObjectMapper();
		FlightServiceResponse response = null;
		try {
			List<Response> totalResponse = mapper.readValue(new File(fileName), new TypeReference<List<Response>>() {
			});
			List<Integer> responseTimeList = new ArrayList<>();
			Map<String, Integer> statusMap = new HashMap<>();
			totalResponse.forEach(value -> {
				if (value.getStatus().startsWith("2")) {
					incrementValue(statusMap, "2xx");
				} else if (value.getStatus().startsWith("4")) {
					incrementValue(statusMap, "4xx");
				} else if (value.getStatus().startsWith("5")) {
					incrementValue(statusMap, "5xx");
				} else {
					incrementValue(statusMap, "Undefined");
				}

				responseTimeList.add(Integer.parseInt(value.getTimeTaken()));

			});
			double average = responseTimeList.stream().mapToDouble(val -> val).average().getAsDouble();
			int min = responseTimeList.stream().min(Integer::compare).orElse(Integer.valueOf(0));
			int max = responseTimeList.stream().max(Integer::compare).orElse(Integer.valueOf(0));
			int total = Optional.ofNullable(statusMap.values().stream().mapToInt(i -> i).sum())
					.orElse(Integer.valueOf(0));
			int success = Optional.ofNullable(statusMap.get("2xx")).orElse(Integer.valueOf(0));
			int notFound = Optional.ofNullable(statusMap.get("4xx")).orElse(Integer.valueOf(0));
			int serverError = Optional.ofNullable(statusMap.get("5xx")).orElse(Integer.valueOf(0));
			response = FlightServiceResponse.builder().totalRequest(total).successRequest(success)
					.notFoundRequest(notFound).serverErrorRequest(serverError).minResponseTime(min).maxResponseTime(max)
					.averageResponseTime(average).build();

		}

		catch (Exception ex) {
			log.error("Exception in getting FlightServiceResponse " + ex.getMessage());
		}
		return response;
	}

}
