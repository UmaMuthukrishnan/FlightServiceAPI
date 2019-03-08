package com.afkl.cases.df.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.afkl.cases.df.common.exceptions.FlightServiceNotFoundException;
import com.afkl.cases.df.model.ErrorDetails;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Throwable.class)
	public HttpEntity<ErrorDetails> handleGlobalException(Throwable t, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(t.getMessage())
				.details(request.getDescription(false)).statusCode(HttpStatus.SERVICE_UNAVAILABLE.toString()).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public HttpEntity<ErrorDetails> handleGlobalException(HttpServerErrorException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(ex.getMessage())
				.details(request.getDescription(false)).statusCode(ex.getStatusCode().toString()).build();
		return new ResponseEntity<>(errorDetails, ex.getStatusCode());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public final HttpEntity<ErrorDetails> handleIllegalArgumentRequest(IllegalArgumentException ex,
			WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(ex.getMessage())
				.details(request.getDescription(false)).statusCode(HttpStatus.BAD_REQUEST.toString()).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FlightServiceNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleServiceNotFoundException(FlightServiceNotFoundException ex,
			WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(ex.getMessage())
				.details(request.getDescription(false)).statusCode(HttpStatus.NOT_FOUND.toString()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InterruptedException.class)
	public final ResponseEntity<ErrorDetails> handleInterruptedException(InterruptedException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(ex.getMessage())
				.details(request.getDescription(false)).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ExecutionException.class)
	public final ResponseEntity<ErrorDetails> handleExecutionException(ExecutionException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(Date.from(Instant.now())).message(ex.getMessage())
				.details(request.getDescription(false)).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
