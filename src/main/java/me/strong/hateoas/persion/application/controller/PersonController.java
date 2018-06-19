package me.strong.hateoas.persion.application.controller;

import me.strong.hateoas.common.exception.EntityNotFoundException;
import me.strong.hateoas.persion.application.model.PersonResource;
import me.strong.hateoas.persion.application.model.PersonResourceAssembler;
import me.strong.hateoas.persion.domain.model.Person;
import me.strong.hateoas.persion.domain.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@RestController
@ExposesResourceFor(Person.class)        //Entity 명시
@RequestMapping("/people")                //Entity URI 명시
public class PersonController {
	private final PersonRepository personRepository;
	
	@Autowired
	public PersonController(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	@Autowired
	private EntityLinks entityLinks;
	
	@Autowired
	private PersonResourceAssembler assembler;
	
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		return ResponseEntity.status(HttpStatus.CREATED).body(personRepository.save(person));
	}
	
	@GetMapping(value = "")
	public ResponseEntity<Page<Person>> readPeople(Pageable pageable) {
		Page<Person> result = this.personRepository.findAll(pageable);
		result.getContent().forEach((person) -> {
//			person.add(entityLinks.linkToSingleResource(Person.class, person.getPersonKey()));
//			person.add(entityLinks.linkToCollectionResource(Person.class));
			person.add(ControllerLinkBuilder.linkTo(this.getClass()).slash(person.getPersonKey()).withSelfRel());
			person.add(ControllerLinkBuilder.linkTo(this.getClass()).withRel("people"));
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/resources")
	public ResponseEntity<List<PersonResource>> readPeopleAsResources(Pageable pageable) {
		Page<Person> result = this.personRepository.findAll(pageable);
		List<PersonResource> resources = this.assembler.toResources(result.getContent());
		return ResponseEntity.ok(resources);
	}
	
	@GetMapping(value = "/{personKey}")
	public ResponseEntity<Person> getPerson(@PathVariable Long personKey) {
		Person person = this.personRepository.findById(personKey).orElse(new Person());
//		person.add(entityLinks.linkToSingleResource(Person.class, personKey));
//		person.add(entityLinks.linkToCollectionResource(Person.class));
		
		try {
			person.add(ControllerLinkBuilder.linkTo(PersonController.class.getMethod("getPerson", Long.class), person.getPersonKey()).withRel("getMethod"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		person.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getPerson(personKey)).withRel("methodOn"));
		
		person.add(ControllerLinkBuilder.linkTo(this.getClass()).slash(person.getPersonKey()).withSelfRel());
		person.add(ControllerLinkBuilder.linkTo(this.getClass()).withRel("people"));
		
		return ResponseEntity.ok(person);
	}
	
	@PutMapping(value = "/{personKey}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> updatePerson(@PathVariable Long personKey, @RequestBody Person person) {
		this.personRepository.findById(personKey).orElseThrow(IllegalArgumentException::new);
		return ResponseEntity.ok(this.personRepository.save(person));
	}
	
	@DeleteMapping(value = "/{personKey}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> deletePerson(@PathVariable Long personKey) {
		Person person = this.personRepository.findById(personKey).orElseThrow(() -> new EntityNotFoundException("Person[" + personKey + "] is n ot found"));
		this.personRepository.deleteById(personKey);
		return ResponseEntity.ok(person);
	}
}
