package me.strong.hateoas.member.interfaces.controller;

import me.strong.hateoas.common.application.model.CustomPageMetadata;
import me.strong.hateoas.member.application.service.MemberSearchService;
import me.strong.hateoas.member.interfaces.model.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author taesu
 * @version 1.0
 * @since 1.0
 */
@RestController
public class MemberSearchController {

    private MemberSearchService memberSearchService;

    public MemberSearchController(MemberSearchService memberSearchService) {
        this.memberSearchService = memberSearchService;
    }

    /**
     * PagedResource로 반환하여야 HAL style의 response가 생성된다
     * 또한 @Relation(collectionRelation = "members")을 Response dto에 선언해서
     * 응답 값에대한 키이름을 설정할 수 있다
     */
    @GetMapping("/members")
    public ResponseEntity<PagedResources<MemberDto.MemberSearchResponse>> searchMembers(Pageable pageable) {
        Page<MemberDto.MemberSearchResponse> page = this.memberSearchService.searchMember(pageable);
        PagedResources<MemberDto.MemberSearchResponse> body
                = new PagedResources<>(page.getContent(),
                                       new CustomPageMetadata(page),
                                       ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).searchMembers(pageable)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaTypes.HAL_JSON)
                .body(body);
    }

    /**
     * Response의 content type을 HAL_JSON_VALUE로 설정하더라도 (설정하지 않아도 가능은 하다)
     * Response 객체로 감싸서 내보내더라도 감싸는 클래스가 ResourceSupport 클래스를 상속해야한다
     * 
     * 만약 이를 따르고 싶지 않다면 application.properties에서 default option을 false로 선언하고
     * content-type을 application/json으로 준다.
     *
     * @see me.strong.hateoas.person.application.controller.PersonController#readPerson(Long) 
     */
    @GetMapping("/members/{memberKey}")
    public ResponseEntity<MemberDto.ResourceSupportedApiResponse> searchMember(@PathVariable("memberKey") Long memberKey) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MemberDto.ResourceSupportedApiResponse("success", this.memberSearchService.searchMember(memberKey)));
    }
}