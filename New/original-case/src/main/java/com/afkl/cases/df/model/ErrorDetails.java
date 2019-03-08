package com.afkl.cases.df.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;
	private String statusCode;

	public ErrorDetails(Date timestamp, String message, String details, String statusCode) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.statusCode = statusCode;
	}

}
