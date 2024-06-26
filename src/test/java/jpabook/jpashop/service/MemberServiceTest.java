package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        System.out.println("-----");
        Long savedId = memberService.join(member);
        System.out.println("-----");
        List<Member> memberList = memberRepository.findByName("kim");

        // then
        // 클래스의 equals method 사용
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
        System.out.println("member == memberList.get(0): " + (member == memberList.get(0))); // true
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        Member member1 = new Member();
        
    }
}