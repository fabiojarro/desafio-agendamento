package br.com.desafio.agenda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.desafio.agenda.bean.ErrorMessage;
import br.com.desafio.agenda.exception.ApiException;


@RestControllerAdvice
public class ApiExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Throwable.class)
	public ErrorMessage handle(Throwable throwable) {
		logger.error(throwable.getMessage(), throwable);
		return new ErrorMessage("Ocorreu um erro desconhecido.");
	}
	
	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<ErrorMessage> handle(ApiException exception) {
		
		logger.error(exception.getMessage(), exception);
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
		
		return ResponseEntity.status(exception.getStatus()).body(errorMessage);
	}
}