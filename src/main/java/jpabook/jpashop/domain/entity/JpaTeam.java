package jpabook.jpashop.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id,", "teamName"})
public class JpaTeam {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<JpaMember> memberList = new ArrayList<>();

    public JpaTeam(String teamName) {
        this.teamName = teamName;
    }
}
