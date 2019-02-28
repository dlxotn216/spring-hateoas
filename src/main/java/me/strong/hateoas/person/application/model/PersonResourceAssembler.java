package me.strong.hateoas.person.application.model;

import me.strong.hateoas.person.application.controller.PersonController;
import me.strong.hateoas.person.domain.model.Person;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@Component
public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, PersonResource> {
	public PersonResourceAssembler() {
		super(PersonController.class, PersonResource.class);
	}
	
	@Override
	public PersonResource toResource(Person entity) {
		PersonResource personResource = super.createResourceWithId(entity.getPersonKey(), entity);
		personResource.setPersonKey(entity.getPersonKey());
		personResource.setName(entity.getName());
		personResource.setBirth(entity.getBirth());
		personResource.add(ControllerLinkBuilder.linkTo(this.getClass()).withRel("people").withHreflang("en").withMedia("application/json"));
		return personResource;
	}
}
