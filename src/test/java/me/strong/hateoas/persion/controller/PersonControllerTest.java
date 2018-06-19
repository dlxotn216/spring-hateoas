package me.strong.hateoas.persion.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.strong.hateoas.common.application.model.ApiResponse;
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

/**
 * Created by Lee Tae Su on 2018-06-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PersonControllerTest {

    @Autowired
    private ObjectMapper objectMapper;


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
        ResponseEntity<ApiResponse> response =
                testRestTemplate.postForEntity("/people", person, ApiResponse.class);

        //Then
        ApiResponse apiResponse = response.getBody();
        Person resultPerson = objectMapper.convertValue(apiResponse.getResult(), new TypeReference<Person>() {
        });
        log.info("createTest -> " + resultPerson.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resultPerson).isNotNull();
        assertThat(resultPerson.getName()).isEqualTo(person.getName());
        assertThat(resultPerson.getBirth()).isEqualTo(person.getBirth());

    }

    @Test
    public void getTest() {
        //Given
        Person person = new Person("Person1", ZonedDateTime.now());
        Person saved = objectMapper.convertValue(testRestTemplate.postForEntity("/people", person, ApiResponse.class).getBody().getResult(), new TypeReference<Person>() {
        });
        assertThat(saved).isNotNull();

        //When
        ResponseEntity<ApiResponse> response
                = testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.GET, new HttpEntity<>(saved, headers), ApiResponse.class);

        //Then
        ApiResponse apiResponse = response.getBody();
        Person resultPerson = objectMapper.convertValue(apiResponse.getResult(), new TypeReference<Person>() {
        });
        log.info("getTest -> " + resultPerson.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(resultPerson.getName()).isEqualTo(person.getName());
        assertThat(resultPerson.getBirth()).isEqualTo(person.getBirth());
    }

    @Test
    public void updateTest() {
        //Given
        Person person = new Person("Person1", ZonedDateTime.now());
        Person saved = objectMapper.convertValue(testRestTemplate.postForEntity("/people", person, ApiResponse.class).getBody().getResult(), new TypeReference<Person>() {
        });
        assertThat(saved).isNotNull();

        //When
        saved.changeName("Change name");
        ResponseEntity<ApiResponse> response
                = testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.PUT, new HttpEntity<>(saved, headers), ApiResponse.class);

        //Then
        ApiResponse apiResponse = response.getBody();
        Person resultPerson = objectMapper.convertValue(apiResponse.getResult(), new TypeReference<Person>() {
        });
        log.info("updateTest -> " + resultPerson.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(resultPerson.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void deleteTest() {
        //Given
        Person person = new Person("Person1", ZonedDateTime.now());
        Person saved = objectMapper.convertValue(testRestTemplate.postForEntity("/people", person, ApiResponse.class).getBody().getResult(), new TypeReference<Person>() {
        });
        assertThat(saved).isNotNull();

        //When
        testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.DELETE, new HttpEntity<>(headers), Person.class);

        //Then
        ResponseEntity<ApiResponse> response
                = testRestTemplate.exchange("/people/" + saved.getPersonKey(), HttpMethod.GET, new HttpEntity<>(headers), ApiResponse.class);

        ApiResponse apiResponse = response.getBody();
        Person resultPerson = objectMapper.convertValue(apiResponse.getResult(), new TypeReference<Person>() {
        });
        log.info("deleteTest -> " + resultPerson.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(resultPerson.getName()).isNullOrEmpty();
    }

    @Test
    public void deleteTestForInvalidPersonKey() {
        //When
        ResponseEntity<ApiResponse> response = testRestTemplate.exchange("/people/-300", HttpMethod.DELETE, new HttpEntity<>(headers), ApiResponse.class);

        //Then
        log.info("deleteTestForInvalidPersonKey -> " + response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}