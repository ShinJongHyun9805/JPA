package jpabook.jpashop.api;

import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/v1/members")
    public List<Member> getMemberListV1() {
        return memberService.findMembers();
    }

    @GetMapping("/v2/members")
    public Result getMemberListV2() {

        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> members = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(members);
    }

    @PostMapping(value = "/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PostMapping("/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequestDto requestDto) {

        // Entity Mapping
        Member member = new Member();
        member.setName(requestDto.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PutMapping("/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") long id, @RequestBody @Valid UpdateMemberRequestDto requestDto) {

        // update
        memberService.update(id, requestDto.getName());

        // update 후 데이터 조회
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

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

    @Data
    private static class UpdateMemberRequestDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    private static class UpdateMemberResponse {
        private long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    private static class MemberDto {
        private String name;
    }

}
