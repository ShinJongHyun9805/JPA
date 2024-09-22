package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class QueryDslTeam {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "queryDslTeam")
    List<QueryDslMember> members = new ArrayList<>();

    public QueryDslTeam(String name) {
        this.name = name;
    }

}
