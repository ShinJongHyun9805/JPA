package jpabook.jpashop.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.QMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class queryDslTest {

    @Autowired
    EntityManager em;

    @Test
    void contextLoad() {

        Member member = new Member();
        member.setName("qwer");
        em.persist(member);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = new QMember("m");

        Member result = query.selectFrom(qMember).fetchOne();

        Assertions.assertThat(result).isEqualTo(member);

    }
}
