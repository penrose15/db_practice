package hello.jdbc.repository;

import hello.jdbc.domain.Members;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MembersRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();



    @Test
    @Transactional
    void crud() throws SQLException {
        //save
        Members members = new Members("memberV0", 10000);
        repository.save(members);

        //findById
        Members findMember = repository.findById(members.getMemberId());
        log.info("findMember={}", findMember);

        log.info("findMember == members ? {}", members == findMember);
        log.info("findMember == members ? {}", members.equals(findMember));

        assertThat(members).isEqualTo(findMember);

        //update : money 10000 -> 20000

        repository.update(members.getMemberId(), 20000);
        Members updateMember = repository.findById(members.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(members.getMemberId());
        assertThatThrownBy(() -> repository.findById(members.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}