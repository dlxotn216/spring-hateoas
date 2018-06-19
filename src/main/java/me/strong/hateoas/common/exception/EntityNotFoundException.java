package me.strong.hateoas.common.exception;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
public class EntityNotFoundException extends RuntimeException {
	
	public EntityNotFoundException(String message){
		super(message);
	}
	
}
