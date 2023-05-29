package hello.jdbc.repository;

import hello.jdbc.domain.Members;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.lang.reflect.Member;
import java.sql.*;
import java.util.NoSuchElementException;

/*
* SQLExceptionTranslator
* */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate template;
    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Members save(Members members) {
        String sql = "insert into members(member_id, money) values(?, ?)";
        template.update(sql, members.getMemberId(), members.getMoney());

        return members;
    }
    @Override
    public Members findById(String memberId) {
        String sql = "select * from members where member_id = ?";

        return template.queryForObject(sql, memberRowMapper() , memberId);
    }

    private RowMapper<Members> memberRowMapper() {
        return (rs, rowNum) -> {
            Members member = new Members();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };

    }
    @Override
    public void update(String memberId, int money) {
        String sql = "update members set money=? where member_id=?";
        template.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from members where member_id = ?";
        template.update(sql, memberId);
    }

}
