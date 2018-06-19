package me.strong.hateoas.runner;

import me.strong.hateoas.persion.domain.model.Person;
import me.strong.hateoas.persion.domain.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@Component
public class AppRunner implements ApplicationRunner {
	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		personRepository.saveAll(Arrays.asList(
				new Person("person1", ZonedDateTime.now()),
				new Person("person2", ZonedDateTime.now()),
				new Person("person3", ZonedDateTime.now())
		))	;
	}
}
