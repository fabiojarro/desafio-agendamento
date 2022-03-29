package br.com.desafio.agenda.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -5468131184191069108L;
	
	private HttpStatus status;
	
	public ApiException(String message) {
		this(message, HttpStatus.BAD_REQUEST);
	}

	public ApiException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	

}
