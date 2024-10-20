package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import jpabook.jpashop.domain.entity.QueryDslMember;
import jpabook.jpashop.domain.entity.QueryDslTeam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
class MemberJpaQueryDslRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberJpaQueryDslRepository memberJpaQueryDslRepository;

    @Test
    void 회원저장() {

        QueryDslMember member = new QueryDslMember("member1", 10);
        em.persist(member);

        QueryDslMember findMemberByMemberId = memberJpaQueryDslRepository.findById(member.getId()).get();
        Assertions.assertEquals(member.getId(), findMemberByMemberId.getId());
    }

    @Test
    void 회원저장_queryDsl() {

        QueryDslMember member = new QueryDslMember("member1", 10);
        em.persist(member);

        List<QueryDslMember> findByUserName = memberJpaQueryDslRepository.findByUserNameForQueryDsl(member.getUserName());
        Assertions.assertEquals(member.getId(), findByUserName.get(0).getId());
    }

    @Test
    void 검색테스트_queryDsl_Builder() {
        QueryDslTeam teamA = new QueryDslTeam("teamA");
        QueryDslTeam teamB = new QueryDslTeam("teamB");
        em.persist(teamA);
        em.persist(teamB);

        QueryDslMember member1 = new QueryDslMember("qwer1", 10, teamA);
        QueryDslMember member2 = new QueryDslMember("qwer2", 20, teamA);

        QueryDslMember member3 = new QueryDslMember("qwer3", 30, teamB);
        QueryDslMember member4 = new QueryDslMember("qwer4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition cond = new MemberSearchCondition();
        cond.setAgeGoe(35);
        cond.setAgeLoe(40);
        cond.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaQueryDslRepository.searchByBuilder(cond);

        Assertions.assertFalse(ObjectUtils.isEmpty(result));
    }

    @Test
    void 검색테스트_queryDsl_Where() {
        QueryDslTeam teamA = new QueryDslTeam("teamA");
        QueryDslTeam teamB = new QueryDslTeam("teamB");
        em.persist(teamA);
        em.persist(teamB);

        QueryDslMember member1 = new QueryDslMember("qwer1", 10, teamA);
        QueryDslMember member2 = new QueryDslMember("qwer2", 20, teamA);

        QueryDslMember member3 = new QueryDslMember("qwer3", 30, teamB);
        QueryDslMember member4 = new QueryDslMember("qwer4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition cond = new MemberSearchCondition();
        cond.setAgeGoe(35);
        cond.setAgeLoe(40);
        cond.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaQueryDslRepository.search(cond);

        Assertions.assertFalse(ObjectUtils.isEmpty(result));
        Assertions.assertTrue(result.stream().anyMatch(MemberTeamDto -> MemberTeamDto.getUserName().equals("qwer4")));
    }
}