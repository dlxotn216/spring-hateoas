package me.strong.hateoas.member.interfaces.controller;

import me.strong.hateoas.member.application.service.MemberCreateService;
import me.strong.hateoas.member.interfaces.model.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taesu at : 2019-02-21
 *
 * @author taesu
 * @version 1.0
 * @since 1.0
 */
@RestController
public class MemberCreateController {
    
    private MemberCreateService memberCreateService;

    public MemberCreateController(MemberCreateService memberCreateService) {
        this.memberCreateService = memberCreateService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDto.MemberCreateResponse> createMember(@RequestBody MemberDto.MemberCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.memberCreateService.create(request));
    }
}
