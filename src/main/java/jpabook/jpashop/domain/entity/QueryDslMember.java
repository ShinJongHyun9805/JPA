package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class QueryDslMember {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String userName;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private QueryDslTeam queryDslTeam;


    public QueryDslMember(String username) {
        this(username, 0);
    }
    public QueryDslMember(String username, int age) {
        this(username, age, null);
    }
    public QueryDslMember(String username, int age, QueryDslTeam team) {
        this.userName = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }
    public void changeTeam(QueryDslTeam team) {
        this.queryDslTeam = team;
        team.getMembers().add(this);
    }

}
