package me.strong.hateoas.persion.domain.repository;

import me.strong.hateoas.persion.domain.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
