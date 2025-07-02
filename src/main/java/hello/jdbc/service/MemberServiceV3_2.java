package hello.jdbc.service;

import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_2 {

	private final TransactionTemplate transactionTemplate;
	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.memberRepository = memberRepository;
	}

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		transactionTemplate.executeWithoutResult((status) -> {
			try {
				//비즈니스 로직
				bizLogic(fromId, toId, money);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		});

	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
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
- private final PlatformTransactionManager transactionManager
	- 트랜잭션 매니저를 주입 받는다.
		- 지금은 JDBC 기술을 사용하기 때문에 DataSourceTransactionManager 구현체를 주입 받아야 한다.
	- 물론 JPA 같은 기술로 변경되면 JpaTransactionManager 를 주입 받으면 된다.
- transactionManager.getTransaction()
	- 트랜잭션을 시작한다.
	- TransactionStatus status 를 반환한다.
		- 현재 트랜잭션의 상태 정보가 포함되어 있다.
		- 이후 트랜잭션을 커밋, 롤백할 때 필요하다.
new DefaultTransactionDefinition()
트랜잭션과 관련된 옵션을 지정할 수 있다. 자세한 내용은 뒤에서 설명한다.
transactionManager.commit(status)
트랜잭션이 성공하면 이 로직을 호출해서 커밋하면 된다.
transactionManager.rollback(status)
문제가 발생하면 이 로직을 호출해서 트랜잭션을 롤백하면 된다.
* */
