package jpabook.jpashop;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.entity.QQueryDslMember;
import jpabook.jpashop.domain.entity.QQueryDslTeam;
import jpabook.jpashop.domain.entity.QueryDslMember;
import jpabook.jpashop.domain.entity.QueryDslTeam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @PersistenceContext
    EntityManager em;

    private JPAQueryFactory queryFactory;

    @Test
    @Transactional
    @Rollback(false)
    void test() {

//        QueryDslTeam teamA = new QueryDslTeam("teamA");
//        QueryDslTeam teamB = new QueryDslTeam("teamB");
//        em.persist(teamA);
//        em.persist(teamB);
//
//        QueryDslMember member = new QueryDslMember("member1", 10, teamA);
//        em.persist(member);
//        QueryDslMember member2 = new QueryDslMember("member2", 20, teamA);
//        em.persist(member2);
//        QueryDslMember member3 = new QueryDslMember("member3", 30, teamB);
//        em.persist(member3);
//        QueryDslMember member4 = new QueryDslMember("member4", 10, teamB);
//        em.persist(member4);
//
//        em.flush();
//        em.clear();

        List<QueryDslMember> queryGetMemberList = em.createQuery("select q from QueryDslMember q", QueryDslMember.class).getResultList();
        System.out.println("queryGetMemberList = " + queryGetMemberList);
    }

    @Test
    void queryDslTest() {
        queryFactory = new JPAQueryFactory(em);
        QQueryDslMember m = QQueryDslMember.queryDslMember;

        QueryDslMember queryGetMember = queryFactory
                .select(m)
                .from(m)
                .where(m.userName.eq("member1"))
                .fetchOne();

        System.out.println("queryGetMember = " + queryGetMember.getUserName());
    }

    @Test
    void queryDslSearchTest() {
        queryFactory = new JPAQueryFactory(em);
        QQueryDslMember m = QQueryDslMember.queryDslMember;

//        QueryDslMember findMember = queryFactory.selectFrom(m)
//                .where(m.userName.eq("member1")
//                        .and(m.age.eq(10)))
//                .fetchOne();

//        Assertions.assertEquals(findMember.getId(), 1);
//        System.out.println("findMember = " + findMember);

        QueryDslMember findMember = queryFactory.selectFrom(m)
                .where(m.userName.endsWith("member1"))
                .fetchOne();

        System.out.println("findMember = " + findMember);
    }

    @Test
    void resultTest() {

        queryFactory = new JPAQueryFactory(em);
        QQueryDslMember m = QQueryDslMember.queryDslMember;

        QueryResults<QueryDslMember> results = queryFactory.selectFrom(m)
                .fetchResults();

    }


    @Test
//    @Commit
    void group() {

//        QueryDslTeam teamA = new QueryDslTeam("teamA");
//        QueryDslTeam teamB = new QueryDslTeam("teamB");
//
//        List<QueryDslTeam> teamList = Arrays.asList(teamA, teamB);
//        teamList.forEach(em::persist);
//
//        QueryDslMember member1 = new QueryDslMember("qwer", 10, teamA);
//        QueryDslMember member2 = new QueryDslMember("asdf", 30, teamB);
//        QueryDslMember member3 = new QueryDslMember("zxcv", 40, teamA);
//        QueryDslMember member4 = new QueryDslMember("tyui", 35, teamB);
//        QueryDslMember member5 = new QueryDslMember("fdsg", 20, teamA);
//        QueryDslMember member6 = new QueryDslMember("vbnh", 60, teamB);
//
//        List<QueryDslMember> memberList = Arrays.asList(member1, member2, member3, member4, member5, member6);
//        memberList.forEach(em::persist);

        queryFactory = new JPAQueryFactory(em);
        QQueryDslMember m = QQueryDslMember.queryDslMember;
        QQueryDslTeam t = QQueryDslTeam.queryDslTeam;

        List<Tuple> result = queryFactory
                .select(t.name, m.age.avg())
                .from(m)
                .join(m.queryDslTeam, t)
                .groupBy(t.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple TeamB = result.get(1);

        Assertions.assertEquals(teamA.get(t.name), "teamA");
    }
}
