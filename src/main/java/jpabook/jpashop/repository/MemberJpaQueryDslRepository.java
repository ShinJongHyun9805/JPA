package jpabook.jpashop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import jpabook.jpashop.domain.dto.QMemberTeamDto;
import jpabook.jpashop.domain.entity.QQueryDslMember;
import jpabook.jpashop.domain.entity.QQueryDslTeam;
import jpabook.jpashop.domain.entity.QueryDslMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;

@Repository
public class MemberJpaQueryDslRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaQueryDslRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(QueryDslMember queryDslMember) {
        em.persist(queryDslMember);
    }

    public Optional<QueryDslMember> findById(Long id) {
        QueryDslMember findByMember = em.find(QueryDslMember.class, id);

        return Optional.ofNullable(findByMember);
    }

    public List<QueryDslMember> findAll() {
        return em.createQuery("select m from QueryDslMember m", QueryDslMember.class)
                .getResultList();
    }

    public List<QueryDslMember> findAllForQueryDsl() {

        QQueryDslMember qQueryDslMember = QQueryDslMember.queryDslMember;

        return queryFactory
                .selectFrom(qQueryDslMember)
                .fetch();
    }

    public List<QueryDslMember> findByUserName(String userName) {
        return em.createQuery("select m from QueryDslMember m where m.userName = :username", QueryDslMember.class)
                .setParameter("username", userName)
                .getResultList();
    }

    public List<QueryDslMember> findByUserNameForQueryDsl(String userName) {
        QQueryDslMember m = QQueryDslMember.queryDslMember;

        return queryFactory.selectFrom(m)
                .where(m.userName.eq(userName))
                .fetch();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        QQueryDslMember m = QQueryDslMember.queryDslMember;
        QQueryDslTeam t = QQueryDslTeam.queryDslTeam;

        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUserName())) {
            builder.and(m.userName.eq(condition.getUserName()));
        }

        if (hasText(condition.getTeamName())) {
            builder.and(t.name.eq(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(m.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(m.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        m.id.as("memberId"),
                        m.userName,
                        m.age,
                        t.id.as("teamId"),
                        t.name.as("teamName")))
                .from(m)
                .leftJoin(m.queryDslTeam, t)
                .where(builder)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        QQueryDslMember m = QQueryDslMember.queryDslMember;
        QQueryDslTeam t = QQueryDslTeam.queryDslTeam;

        return queryFactory
                .select(new QMemberTeamDto(
                        m.id.as("memberId"),
                        m.userName,
                        m.age,
                        t.id.as("teamId"),
                        t.name.as("teamName")))
                .from(m)
                .leftJoin(m.queryDslTeam, t)
                .where(userNameEq(condition.getUserName(), m),
                        teamNameEq(condition.getTeamName(), t),
                        ageGoe(condition.getAgeLoe(), m),
                        ageLoe(condition.getAgeLoe(), m))
                .orderBy(m.id.desc())
                .fetch();
    }

    private BooleanExpression userNameEq(String userName, QQueryDslMember m) {
        return isEmpty(userName) ? null : m.userName.eq(userName);
    }

    private BooleanExpression teamNameEq(String teamName, QQueryDslTeam t) {
        return isEmpty(teamName) ? null : t.name.eq(teamName);
    }

    private BooleanExpression ageGoe(Integer ageGoe, QQueryDslMember m) {
        return isEmpty(ageGoe) ? null : m.age.goe(ageGoe) ;
    }

    private BooleanExpression ageLoe(Integer ageLoe, QQueryDslMember m) {
        return isEmpty(ageLoe) ? null : m.age.loe(ageLoe);
    }
}
