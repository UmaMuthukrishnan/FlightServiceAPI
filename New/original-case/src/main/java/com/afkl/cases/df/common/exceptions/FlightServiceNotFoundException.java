package com.afkl.cases.df.common.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FlightServiceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FlightServiceNotFoundException(String exception) {
	    super(exception);
	  }

}
