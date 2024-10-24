package jpabook.jpashop.repository;

import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface memberJpaQueryDslCustomRepository {

    List<MemberTeamDto> search(MemberSearchCondition cond);

    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition cond, Pageable pageable);


    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition cond, Pageable pageable);
}
