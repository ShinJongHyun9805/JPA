package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import jpabook.jpashop.domain.entity.QueryDslMember;
import jpabook.jpashop.domain.entity.QueryDslTeam;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaQueryDslRepository2Test {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberJpaQueryDslRepository2 memberJpaQueryDslRepository;

    @Test
    void basicTest() {
        QueryDslMember member = new QueryDslMember("member1", 10);
        memberJpaQueryDslRepository.save(member);

        QueryDslMember findMember = memberJpaQueryDslRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<QueryDslMember> findAllMember = memberJpaQueryDslRepository.findAll();
        assertThat(findAllMember).containsExactly(member);

        List<QueryDslMember> findMemberByUserName = memberJpaQueryDslRepository.findByUserName(member.getUserName());
        assertThat(findMemberByUserName).containsExactly(member);
    }

    @Test
    void searchTest() {

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

        assertThat(result).extracting("userName").containsExactly("qwer4");
    }

    @Test
    void searchPageSimpleTest() {

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
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<MemberTeamDto> result = memberJpaQueryDslRepository.searchPageSimple(cond, pageRequest);

        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getContent()).extracting("userName").containsExactly("qwer3");
    }

}