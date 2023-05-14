package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Members;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MembersRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        //기본 Drivermanager -> 항상 새로운 커넥션 제공
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setPoolName("hikari-connection-pool");

        repository = new MemberRepositoryV1(dataSource);
    }



    @Test
    @Transactional
    void crud() throws SQLException, InterruptedException {
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

        Thread.sleep(2000);
    }
}