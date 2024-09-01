package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.entity.JpaMember;
import jpabook.jpashop.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void delete(JpaMember member) {
        em.remove(member);
    }

    public List<JpaMember> findAllForJpaMember() {
        return em.createQuery("select m from Member m", JpaMember.class)
                .getResultList();
    }

    public Optional<JpaMember> findByIdForJpaMember(Long id) {
        JpaMember jpaMember = em.find(JpaMember.class, id);
        return Optional.ofNullable(jpaMember);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

}
