package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id,", "userName", "age"})
public class JpaMember extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String userName;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private JpaTeam team;

    public JpaMember (String userName) {
        this.userName = userName;
    }

    public JpaMember (String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    public JpaMember (String userName, int age, JpaTeam team) {
        this.userName = userName;
        this.age = age;

        if (!ObjectUtils.isEmpty(team)) {
            changeTeam(team);
        }
    }

    public void changeTeam(JpaTeam team) {
        this.team = team;
        team.getMemberList().add(this);
    }
}
