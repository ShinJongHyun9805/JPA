package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.entity.QueryDslMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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


}