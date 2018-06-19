package me.strong.hateoas.persion.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@Entity
@Table
@Getter
@NoArgsConstructor
@SequenceGenerator(name = Person.GENERATOR_NAME, sequenceName = "PERSON_SEQ")
public class Person extends ResourceSupport{
	static final String GENERATOR_NAME = "PersonSeq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Person.GENERATOR_NAME)
	private Long personKey;
	
	private String name;
	
	private ZonedDateTime birth;
		
	public Person(String name, ZonedDateTime birth) {
		this.name = name;
		this.birth = birth;
	}
	
	public void changeName(String name) {
		if(StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("name must not be empty " + name);
		}
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Person{" +
				"personKey=" + personKey +
				", name='" + name + '\'' +
				", birth=" + birth +
				", links=" + super.toString() +
				'}';
	}
}
