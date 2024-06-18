package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PostMapping
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequestDto requestDto) {

        // Entity Mapping
        Member member = new Member();
        member.setName(requestDto.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @Data
    private static class CreateMemberRequestDto {
        private String name;
    }

    @Data
    private static class CreateMemberResponse {
        private Long id;

        private CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
