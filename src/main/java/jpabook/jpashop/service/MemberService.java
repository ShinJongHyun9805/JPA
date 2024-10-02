package jpabook.jpashop.service;

import jpabook.jpashop.domain.entity.JpaMember;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.repository.MemberJpaRepository;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // mock test
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(long id, String name) {

        // DB 조회 = 영속상태
        Member member = memberRepository.findOne(id);
        member.setName(name); // -> 커밋하면서 더티 체킹으로 UPDATE 쿼리

    }

    public JpaMember findMemberByUserName(String userName) {
        return memberJpaRepository.findByUserName(userName);
    }

}
