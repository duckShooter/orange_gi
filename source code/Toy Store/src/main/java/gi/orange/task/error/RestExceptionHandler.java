package gi.orange.task.error;

import org.springframework.security.core.AuthenticationException;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code=HttpStatus.UNPROCESSABLE_ENTITY)
	public @ResponseBody ErrorResponse handleConstraintViolationException (
			ConstraintViolationException exception, WebRequest request) {
		final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		final String requestPath = ((ServletWebRequest)request).getRequest().getRequestURI();
		return new ErrorResponse(status.value(), status.getReasonPhrase(), 
				"Some required fields are missing or invalid", requestPath);
		
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(code=HttpStatus.UNPROCESSABLE_ENTITY)
	public @ResponseBody ErrorResponse handleEmptyResultDataAccessException (
			EmptyResultDataAccessException exception, WebRequest request) {
		final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		final String requestPath = ((ServletWebRequest)request).getRequest().getRequestURI();
		return new ErrorResponse(status.value(), 
				status.getReasonPhrase(), exception.getMessage(), requestPath); 
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(code=HttpStatus.UNAUTHORIZED)
	public @ResponseBody ErrorResponse handleAuthenticationException (
			AuthenticationException exception, WebRequest request) {
		final HttpStatus status = HttpStatus.UNAUTHORIZED;
		final String requestPath = ((ServletWebRequest)request).getRequest().getRequestURI();
		return new ErrorResponse(status.value(), 
				status.getReasonPhrase(), exception.getMessage(), requestPath); 
	}
}
