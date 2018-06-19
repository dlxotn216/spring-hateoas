package me.strong.hateoas.common.application.controller;

import me.strong.hateoas.common.application.model.ApiResponse;
import me.strong.hateoas.common.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<ApiResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
		return ResponseEntity.badRequest().body(ApiResponse.fromErrorResponse(e.getMessage()));
	}
	
}
