package hello.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.support.JdbcUtils;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

/**
 *  JDBC - ConnectionParam
 */
@Slf4j
public class MemberRepositoryV2 {
    private final DataSource dataSource;

	public MemberRepositoryV2(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public Member save(Member member) throws SQLException {
		String sql ="insert into member(member_id, money) values(?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setInt(2, member.getMoney());
			pstmt.executeUpdate();
			return member;
		} catch (SQLException e) {
			log.error("db error",e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}
	}

	public Member findById(String memberId) throws SQLException {
		String sql = "select * from member where member_id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Member member = new Member();
				member.setMemberId(rs.getString("member_id"));
				member.setMoney(rs.getInt("money"));
				return member;
			} else {
				throw new NoSuchElementException("member not found memberId =" + memberId);
			}
		} catch (SQLException e) {
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, rs);
		}
	}

	public Member findById(Connection con, String memberId) throws
		SQLException {
		String sql = "select * from member where member_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Member member = new Member();
				member.setMemberId(rs.getString("member_id"));
				member.setMoney(rs.getInt("money"));
				return member;
			} else {
				throw new NoSuchElementException("member not found memberId="
					+ memberId);
			}
		} catch (SQLException e) {
			log.error("db error", e);
			throw e;
		} finally {
			//connection은 여기서 닫지 않는다.
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
		}
	}

	//회원 수정
	public void update(Connection con,String memberId, int money) throws SQLException {

		String sql = "update member set money=? where member_id=?";

		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}
	}



	//delete - 회원 삭제

	public void delete(String memberId) throws SQLException {
		String sql = "delete from member where member_id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}
	}

    private static void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
/*
- Connection con = dataSource.getConnection();
	- 트랜잭션을 시작하려면 커넥션이 필요하다.
- con.setAutoCommit(false); //트랜잭션 시작
	- 트랜잭션을 시작하려면 자동 커밋 모드를 꺼야한다.
	- 이렇게 하면 커넥션을 통해 세션에 set autocommit false 가 전달되고, 이후부터는 수동 커밋 모드로 동작한다.
	- 이렇게 자동 커밋 모드를 수동 커밋 모드로 변경하는 것을 트랜잭션을 시작한다고 보통 표현한다.
- bizLogic(con, fromId, toId, money);
	- 트랜잭션이 시작된 커넥션을 전달하면서 비즈니스 로직을 수행한다.
	- 이렇게 분리한 이유는 트랜잭션을 관리하는 로직과 실제 비즈니스 로직을 구분하기 위함이다.
- memberRepository.update(con..)
	- 비즈니스 로직을 보면 리포지토리를 호출할 때 커넥션을 전달하는 것을 확인할 수 있다.
- con.commit(); //성공시 커밋
	- 비즈니스 로직이 정상 수행되면 트랜잭션을 커밋한다.
- con.rollback(); //실패시 롤백
	- catch(Ex){..} 를 사용해서 비즈니스 로직 수행 도중에 예외가 발생하면 트랜잭션을 롤백한다.
- release(con);
	- finally {..} 를 사용해서 커넥션을 모두 사용하고 나면 안전하게 종료한다.
	그런데 커넥션 풀을 사용하면 con.close() 를 호출 했을 때 커넥션이 종료되는 것이 아니라 풀에 반납된다.
	현재 수동 커밋 모드로 동작하기 때문에 풀에 돌려주기 전에 기본 값인 자동 커밋 모드로 변경하는 것이 안전하다.
* */
