package jpabook.jpashop.controller;

import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import jpabook.jpashop.repository.MemberJpaQueryDslRepository;
import jpabook.jpashop.repository.MemberJpaQueryDslRepository2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QueryDslMemberController {

    private final MemberJpaQueryDslRepository memberJpaQueryDslRepository;

    private final MemberJpaQueryDslRepository2 memberJpaQueryDslRepository2;

    @GetMapping("/v1/query-dsl-members")
    public List<MemberTeamDto> searchQueryDslMemberV1(MemberSearchCondition cond) {
        return memberJpaQueryDslRepository.search(cond);
    }

    @GetMapping("/v2/query-dsl-members")
    public Page<MemberTeamDto> searchQueryDslMemberV2(MemberSearchCondition cond, Pageable pageable) {
        return memberJpaQueryDslRepository2.searchPageSimple(cond, pageable);
    }

    @GetMapping("/v3/query-dsl-members")
    public Page<MemberTeamDto> searchQueryDslMemberV3(MemberSearchCondition cond, Pageable pageable) {
        return memberJpaQueryDslRepository2.searchPageComplex(cond, pageable);
    }
}
