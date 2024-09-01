package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.entity.JpaMember;
import jpabook.jpashop.domain.entity.JpaTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private final EntityManager em;

    public JpaTeam save(JpaTeam team) {
        em.persist(team);

        return team;
    }

    public void delete(JpaTeam team) {
        em.remove(team);
    }

    public List<JpaTeam> findAll() {
        return em.createQuery("select t from JpaTeam t", JpaTeam.class).getResultList();
    }

    public Optional<JpaTeam> findById(Long id) {
        JpaTeam team = em.find(JpaTeam.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return em.createQuery("select count(t) from JpaMember t", Long.class).getSingleResult();
    }

}
