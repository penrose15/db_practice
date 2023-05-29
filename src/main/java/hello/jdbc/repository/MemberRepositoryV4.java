package hello.jdbc.repository;

import hello.jdbc.domain.Members;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/*
* 예외 누수 문제 해결
* 체크예외 -> 언체크 예외
* MemberRepository 인터페이스 사용
* throws SQLException 제거
* */
@Slf4j
public class MemberRepositoryV4 implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryV4(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Members save(Members members) {
        String sql = "insert into members(member_id, money) values(?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, members.getMemberId());
            pstmt.setInt(2, members.getMoney());
            pstmt.executeUpdate(); //쿼리 실행 후 영향받은 row 수를 반환한다.
            return members;
        } catch (SQLException e) {
            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null);

        }
    }
    @Override
    public Members findById(String memberId) {
        String sql = "select * from members where member_id = ?";

        Connection con = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstat = con.prepareStatement(sql);
            pstat.setString(1, memberId);
            rs = pstat.executeQuery();
            if(rs.next()) { //data가 있으면
                Members member = new Members();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found by memberId : " + memberId);
            }
        } catch (SQLException e) {
            throw new MyDbException(e);
        } finally {
            close(con, pstat,rs);
        }
    }


    @Override
    public void update(String memberId, int money) {
        String sql = "update members set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null);

        }
    }



    private void close(Connection con, Statement st, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(st);
        //주의 : 트랜잭션 동기화 사용하려면 DatasourceUtils를 사용해야 한다.
        DataSourceUtils.releaseConnection(con, dataSource);
    }
    @Override
    public void delete(String memberId) {
        String sql = "delete from members where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate(); //쿼리 실행 후 영향받은 row 수를 반환한다.
        } catch (SQLException e) {
            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null);

        }
    }



    private Connection getConnection() throws SQLException {
        //주의 트랜잭션 동기화를 사용하려면 DatasourceUtils를 사용해야 한다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("getConnection={}, class={}", con, con.getClass());
        return con;
    }
}
