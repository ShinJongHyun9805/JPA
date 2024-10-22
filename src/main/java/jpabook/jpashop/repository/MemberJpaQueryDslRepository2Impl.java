package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.dto.MemberSearchCondition;
import jpabook.jpashop.domain.dto.MemberTeamDto;
import jpabook.jpashop.domain.dto.QMemberTeamDto;
import jpabook.jpashop.domain.entity.QQueryDslMember;
import jpabook.jpashop.domain.entity.QQueryDslTeam;
import jpabook.jpashop.domain.entity.QueryDslMember;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

public class MemberJpaQueryDslRepository2Impl implements memberJpaQueryDslCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaQueryDslRepository2Impl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<MemberTeamDto> search(MemberSearchCondition cond) {

        QQueryDslMember member = QQueryDslMember.queryDslMember;
        QQueryDslTeam team = QQueryDslTeam.queryDslTeam;

        List<MemberTeamDto> findMember = queryFactory
                .select(new QMemberTeamDto(
                                member.id.as("memberId")
                                , member.userName.as("userName")
                                , member.age.as("age")
                                , team.id.as("teamId")
                                , team.name.as("teamName")
                        )
                )
                .from(member)
                .leftJoin(member.queryDslTeam, team)
                .where(
                        userNameEq(cond.getUserName(), member),
                        teamNameEq(cond.getTeamName(), team),
                        ageGoe(cond.getAgeLoe(), member),
                        ageLoe(cond.getAgeLoe(), member))
                .orderBy(member.id.desc())
                .fetch();

        return findMember;
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
