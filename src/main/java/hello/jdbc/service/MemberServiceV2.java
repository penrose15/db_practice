package hello.jdbc.service;

import hello.jdbc.domain.Members;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); //transaction start
            bizLogic(con, fromId, toId, money);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }




    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Members fromMember = memberRepository.findById(con,fromId);
        Members toMember = memberRepository.findById(con,toId);

        memberRepository.update(con,fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con,toId,toMember.getMoney() + money);
    }

    private void validation(Members toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection con) {
        try {
            con.setAutoCommit(true); //connection pool 고려
            con.close();
        } catch (Exception e) {
            log.info("error", e);
        }
    }
}
