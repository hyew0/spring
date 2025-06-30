package hello.jdbc.service;

import java.sql.SQLException;

import hello.jdbc.MemberRepositoryV1;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {
	private final MemberRepositoryV1 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		memberRepository.update(fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {

		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}
}

/*
- formId 의 회원을 조회해서 toId 의 회원에게 money 만큼의 돈을 계좌이체 하는 로직이다.
	- fromId 회원의 돈을 money 만큼 감소한다. UPDATE SQL 실행
	- toId 회원의 돈을 money 만큼 증가한다. UPDATE SQL 실행
- 예외 상황을 테스트해보기 위해 toId 가 "ex" 인 경우 예외를 발생한다.
* */
