package me.strong.hateoas.common.controller;

import me.strong.hateoas.common.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@RestControllerAdvice
public class CommonControllerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = EntityNotFoundException.class)
	public Map<String, Object> entityNotFoundExceptionHandler(EntityNotFoundException e) {
		return Collections.singletonMap("result", e.getMessage());
	}
	
}
