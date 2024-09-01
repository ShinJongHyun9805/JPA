package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.entity.JpaMember;
import jpabook.jpashop.domain.entity.JpaTeam;
import jpabook.jpashop.domain.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberJpaRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private TeamJpaRepository teamJpaRepository;

    @Test
    void testMember() {
        Member member = new Member();
        member.setName("memberA");

//        Member savedMember = memberJpaRepository.save(member);
//        Member findMember = memberJpaRepository.findById(savedMember.getId()).get();
//
//        Assertions.assertEquals(findMember, savedMember);
//        Assertions.assertEquals(findMember, savedMember);
    }

    @Test
    void testEntity() {

        JpaTeam teamA = new JpaTeam("teamA");
        JpaTeam teamB = new JpaTeam("teamB");

        em.persist(teamA);
        em.persist(teamB);

        JpaMember member1 = new JpaMember("member1", 10, teamA);
        JpaMember member2 = new JpaMember("member2", 20, teamA);
        JpaMember member3 = new JpaMember("member3", 30, teamB);
        JpaMember member4 = new JpaMember("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush(); // 영속성 컨텍스트에 쌓인 데이터 DB에 보냄
        em.clear();

        List<JpaMember> members = em.createQuery("select m from JpaMember m", JpaMember.class).getResultList();
        members.forEach(res -> {
            System.out.println("========================= member1 =========================  " + res);
            System.out.println("========================= member.team =========================  " + res.getTeam().getTeamName());
        });
    }

    @Test
    void basicCRUD() {

        JpaMember member1 = new JpaMember("member1" );
        JpaMember member2 = new JpaMember("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        JpaMember findMember1 = memberJpaRepository.findById(member1.getId()).get();
        JpaMember findMember2 = memberJpaRepository.findById(member2.getId()).get();

        Assertions.assertEquals(findMember1, member1);
        Assertions.assertEquals(findMember2, member2);

        List<JpaMember> all = memberJpaRepository.findAll();
        Assertions.assertEquals(all.size(), 2);

        memberJpaRepository.delete(member1);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {

        JpaMember member1 = new JpaMember("member1", 15);
        JpaMember member2 = new JpaMember("member2", 20);

        List<JpaMember> result = memberJpaRepository.findByUserNameAndAgeGreaterThan("member1", 15);

        result.forEach(System.out::println);

    }

    @Test
    void findTop3HelloBy() {

        List<JpaMember> result = memberJpaRepository.findTop3HelloBy();

        result.forEach(System.out::println);
    }

    @Test
    void findUser() {

        JpaMember member1 = new JpaMember("member1", 15);
        JpaMember member2 = new JpaMember("member2", 20);
        em.persist(member1);
        em.persist(member2);

        List<JpaMember> byNames = memberJpaRepository.findByNames(Arrays.asList("member1", "member2"));
        byNames.forEach(System.out::println);
    }

    @Test
    void paging() {

        memberJpaRepository.save(new JpaMember("member1", 10));
        memberJpaRepository.save(new JpaMember("member2", 10));
        memberJpaRepository.save(new JpaMember("member3", 10));
        memberJpaRepository.save(new JpaMember("member4", 10));
        memberJpaRepository.save(new JpaMember("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));
        Page<JpaMember> result = memberJpaRepository.findByAgePageType(age, pageRequest);

        System.out.println("result.getContent() = " + result.getContent());
        System.out.println("result.getTotalElements() = " + result.getTotalElements()); // total Count
        System.out.println("result.getTotalPages() = " + result.getTotalPages());
    }

    @Test
    void entityGraph() {

        JpaTeam teamA = new JpaTeam("teamA");
        JpaTeam teamB = new JpaTeam(("teamB"));
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        JpaMember member1 = new JpaMember("member1", 10, teamA);
        JpaMember member2 = new JpaMember("member2", 15, teamB);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        em.flush();
        em.clear();

        List<JpaMember> result = memberJpaRepository.findAll();
        result.forEach(res -> {
            System.out.println("res.getUserName() = " + res.getUserName());
            System.out.println("res.getTeam().getClass() = " + res.getTeam().getClass());
            System.out.println("res.getTeam().getTeamName() = " + res.getTeam().getTeamName());
        });
    }

    @Test
    void queryHint() {

        JpaMember member1 = new JpaMember("member1", 10);
        JpaMember member2 = new JpaMember("member2", 15);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        em.flush();
        em.clear();

        JpaMember result = memberJpaRepository.findReadOnlyByUserName("member1");
        result.setUserName("member2");

        em.flush(); // -> 를 해도 동기화 X
    }
}