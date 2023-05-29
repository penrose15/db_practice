package hello.jdbc.service;

import hello.jdbc.domain.Members;
import hello.jdbc.repository.MemberRepository;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/*
* 예외 누수 해결
* throw SQLException 제거
*
* MemberRepository에 의존
* */
@Slf4j
@Transactional //이거 하나로 끝....
public class MemberServiceV4 {
    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money)  {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money)  {
        Members fromMember = memberRepository.findById(fromId);
        Members toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId,toMember.getMoney() + money);
    }

    private void validation(Members toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
