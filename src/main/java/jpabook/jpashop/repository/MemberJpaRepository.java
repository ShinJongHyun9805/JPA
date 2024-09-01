package jpabook.jpashop.repository;

import jakarta.persistence.QueryHint;
import jpabook.jpashop.domain.entity.JpaMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  MemberJpaRepository extends JpaRepository<JpaMember, Long> {

    List<JpaMember> findByUserNameAndAgeGreaterThan(String userName, int age);

    List<JpaMember> findTop3HelloBy();

    @Query("select m from JpaMember m where m.userName = :userName and m.age = :age")
    List<JpaMember> findUser(@Param("userName") String userName, @Param("age") int age);

    @Query("select m from JpaMember m where m.userName in :names")
    List<JpaMember> findByNames(@Param("names") List<String> names);

    //---------------------------------------------------------------------------------------------------------------------

    /**
     * paging type method
     *
     * 실무 TIP : 조회해야하는 데이터가 복잡한 경우(JOIN), 원 데이터 조회 쿼리, 카운트 용 쿼리를 분리
     * */
    @Query(value = "select m from JpaMember m left join m.team t",
           countQuery = "select count(m) from JpaMember m")
    Page<JpaMember> findByAgePageType(int age, Pageable pageable);
    //    Slice<JpaMember> findByAgeSliceType(int age, Pageable pageable);
    //    List<JpaMember> findByAgeListType(int age, Pageable pageable);

    //---------------------------------------------------------------------------------------------------------------------
    /**
     * 순수 JPA_Fetch Join == Spring data JPA_@EntityGraph
     * */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<JpaMember> findAll();

    //---------------------------------------------------------------------------------------------------------------------
    /**
     * @Transactional(read-only) == Spring data JPA_@QueryHint
     * */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    JpaMember findReadOnlyByUserName(String userName);

}
