package hello.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

	private final DataSource dataSource;
	private final MemberRepositoryV2 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Connection con = dataSource.getConnection();

		try {
			con.setAutoCommit(false); // 트랜잭션 시작
			//비즈니스 로직
			bizLogic(con, fromId, toId, money);
			con.commit(); //성공시 커밋
		} catch (Exception e) {
			con.rollback(); //실패시 롤백
			throw new IllegalStateException(e);
		} finally {
			release(con);
		}

	}

	private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(con, fromId);
		Member toMember = memberRepository.findById(con, toId);

		memberRepository.update(con, fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(con, toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {

		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}

	private void release(Connection con) {
		if (con != null) {
			try {
				con.setAutoCommit(true); //커넥션 풀 고려
				con.close();
			} catch (Exception e) {
				log.info("error", e);
			}
		}
	}
}

/*
- formId 의 회원을 조회해서 toId 의 회원에게 money 만큼의 돈을 계좌이체 하는 로직이다.
	- fromId 회원의 돈을 money 만큼 감소한다. UPDATE SQL 실행
	- toId 회원의 돈을 money 만큼 증가한다. UPDATE SQL 실행
- 예외 상황을 테스트해보기 위해 toId 가 "ex" 인 경우 예외를 발생한다.
* */
