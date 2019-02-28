package me.strong.hateoas.person.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.time.ZonedDateTime;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@Getter
@Setter
@NoArgsConstructor
public class PersonResource extends ResourceSupport {
	private Long personKey;	
	private String name;	
	private ZonedDateTime birth;
}
