package me.strong.hateoas.persion.application.controller;

import me.strong.hateoas.common.application.controller.AbstractController;
import me.strong.hateoas.common.application.model.ApiResponse;
import me.strong.hateoas.common.exception.EntityNotFoundException;
import me.strong.hateoas.persion.application.model.PersonResource;
import me.strong.hateoas.persion.application.model.PersonResourceAssembler;
import me.strong.hateoas.persion.domain.model.Person;
import me.strong.hateoas.persion.domain.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class PersonController extends AbstractController {
    private final PersonRepository personRepository;

    @Autowired
    private PersonResourceAssembler assembler;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createPerson(@RequestBody Person person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.fromSuccessResult(personRepository.save(person)));
    }

    @GetMapping(value = "")
    public ResponseEntity<ApiResponse> readPeople(Pageable pageable) {
        Page<Person> result = this.personRepository.findAll(pageable);
        result.getContent().forEach((person) -> {
            person.add(ControllerLinkBuilder.linkTo(this.getClass()).slash(person.getPersonKey()).withSelfRel());
            person.add(ControllerLinkBuilder.linkTo(this.getClass()).withRel("people"));
        });
        return ResponseEntity.ok(ApiResponse.fromSuccessResult(result));
    }

    @GetMapping(value = "/resources")
    public ResponseEntity<ApiResponse> readPeopleAsResources(Pageable pageable) {
        Page<Person> result = this.personRepository.findAll(pageable);
        List<PersonResource> resources = this.assembler.toResources(result.getContent());
        return ResponseEntity.ok(ApiResponse.fromSuccessResult(resources));
    }

    @GetMapping(value = "/{personKey}")
    public ResponseEntity<ApiResponse> readPerson(@PathVariable Long personKey) {
        Person person = this.personRepository.findById(personKey).orElse(new Person());

        try {
            person.add(ControllerLinkBuilder.linkTo(PersonController.class.getMethod("getPerson", Long.class), person.getPersonKey()).withRel("getMethod"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        person.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).readPerson(personKey)).withRel("methodOn"));

        person.add(ControllerLinkBuilder.linkTo(this.getClass()).slash(person.getPersonKey()).withSelfRel());
        person.add(ControllerLinkBuilder.linkTo(this.getClass()).withRel("people"));

        return ResponseEntity.ok(ApiResponse.fromSuccessResult(person));
    }

    @PutMapping(value = "/{personKey}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updatePerson(@PathVariable Long personKey, @RequestBody Person person) {
        this.personRepository.findById(personKey).orElseThrow(IllegalArgumentException::new);
        return ResponseEntity.ok(ApiResponse.fromSuccessResult(this.personRepository.save(person)));
    }

    @DeleteMapping(value = "/{personKey}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deletePerson(@PathVariable Long personKey) {
        Person person = this.personRepository.findById(personKey).orElseThrow(() -> new EntityNotFoundException("Person[" + personKey + "] is n ot found"));
        this.personRepository.deleteById(personKey);
        return ResponseEntity.ok(ApiResponse.fromSuccessResult(person));
    }
}
