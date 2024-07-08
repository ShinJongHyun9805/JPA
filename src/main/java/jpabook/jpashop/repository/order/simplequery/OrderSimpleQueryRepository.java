package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 유지 보수를 위해 기존 Repository는 순수 Entity 조회용으로, 해당 Repository는 SELECT 결과가 정해진 dto 위주로 분리.
 * */

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "SELECT new jpabook.jpashop.domain.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " FROM Order o" +
                        " JOIN o.member m" +
                        " JOIN o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}

