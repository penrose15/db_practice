package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Members;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Member;
import java.sql.*;
import java.util.NoSuchElementException;

/*
* JDBC - DriverManager 사용
* */
@Slf4j
public class MemberRepositoryV0 {
    public Members save(Members members) throws SQLException {
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
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }
    }

    public Members findById(String memberId) throws SQLException {
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
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstat,rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
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
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }
    }

    private void close(Connection con, Statement st, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                log.info("error" ,e);
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from members where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate(); //쿼리 실행 후 영향받은 row 수를 반환한다.
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }
    }



    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}