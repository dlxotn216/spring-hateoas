package me.strong.hateoas.persion.controller;

import lombok.extern.slf4j.Slf4j;
import me.strong.hateoas.persion.domain.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by Lee Tae Su on 2018-06-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PersonControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private static final MultiValueMap<String, String> headers;
	
	static {
		headers = new LinkedMultiValueMap<>();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
	}
	
	@Test
	public void createTest() {
		//Given
		Person person = new Person("Person1", ZonedDateTime.now());
		
		//When
		ResponseEntity<Person> response =
				testRestTemplate.postForEntity("/people", person, Person.class);
		
		//Then
		log.info("createTest -> " + response.toString());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(person.getName());
		assertThat(response.getBody().getBirth()).isEqualTo(person.getBirth());
		
	}
	
	@Test
	public void getTest(){
		//Given
		Person person = new Person("Person1", ZonedDateTime.now());
		Person saved = testRestTemplate.postForEntity("/people", person, Person.class).getBody();
		assertThat(saved).isNotNull();
		
		//When		
		ResponseEntity<Person> response
				= testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.GET, new HttpEntity<>(saved, headers), Person.class);
		
		//Then
		log.info("getTest -> " + response.getBody());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(person.getName());
		assertThat(response.getBody().getBirth()).isEqualTo(person.getBirth());
	}
	
	@Test
	public void updateTest() {
		//Given
		Person person = new Person("Person1", ZonedDateTime.now());
		Person saved = testRestTemplate.postForEntity("/people", person, Person.class).getBody();
		assertThat(saved).isNotNull();
		
		//When		
		saved.changeName("Change name");
		ResponseEntity<Person> response
				= testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.PUT, new HttpEntity<>(saved, headers), Person.class);
		
		//Then
		log.info("updateTest -> " + response.toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(saved.getName());
	}
	
	@Test
	public void deleteTest() {
		//Given
		Person person = new Person("Person1", ZonedDateTime.now());
		Person saved = testRestTemplate.postForEntity("/people", person, Person.class).getBody();
		assertThat(saved).isNotNull();
		
		//When		
		testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.DELETE, new HttpEntity<>(headers), Person.class);
				
		//Then
		ResponseEntity<Person> response
				= testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.GET, new HttpEntity<>(headers), Person.class);
		
		log.info("deleteTest -> " + response.toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isNullOrEmpty();
	}
	
	@Test
	public void deleteTestForInvalidPersonKey(){
		//When		
		ResponseEntity response = testRestTemplate.exchange("/people/-300", HttpMethod.DELETE, new HttpEntity<>(headers), Object.class);
		
		//Then
		log.info("deleteTestForInvalidPersonKey -> " + response.toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
	}
	
	
}