package jpabook.jpashop.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.entity.QueryDslMember;
import jpabook.jpashop.domain.entity.QueryDslTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitQueryDslMember {


    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    static class InitService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {

            QueryDslTeam teamA = new QueryDslTeam("teamA");
            QueryDslTeam teamB = new QueryDslTeam("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i=0; i<=100; i++) {
                QueryDslTeam selectedTeam = i % 2 == 0 ? teamA : teamB;

                em.persist(new QueryDslMember("member" + i, i, selectedTeam));
            }
        }
    }
}
