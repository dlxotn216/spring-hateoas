package me.strong.hateoas.runner;

import me.strong.hateoas.member.domain.model.Member;
import me.strong.hateoas.member.domain.repository.MemberRepository;
import me.strong.hateoas.person.domain.model.Person;
import me.strong.hateoas.person.domain.repository.PersonRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Lee Tae Su
 * @version 1.0
 * @project hateoas
 * @since 2018-06-19
 */
@Component
public class AppRunner implements ApplicationRunner {
    private PersonRepository personRepository;
    private MemberRepository memberRepository;

    public AppRunner(PersonRepository personRepository, MemberRepository memberRepository) {
        this.personRepository = personRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        personRepository.saveAll(Arrays.asList(
                new Person("person1", ZonedDateTime.now()),
                new Person("person2", ZonedDateTime.now()),
                new Person("person3", ZonedDateTime.now())
        ));

        memberRepository.saveAll(IntStream.rangeClosed(1, 100)
                                         .mapToObj(value -> new Member("taesu" + value, "Lee Teae Su " + value))
                                         .collect(Collectors.toList()));
    }
}
