package jpabook.jpashop.repository;

import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;

import java.util.List;

public interface memberJpaQueryDslCustomRepository {

    List<MemberTeamDto> search(MemberSearchCondition cond);

}
