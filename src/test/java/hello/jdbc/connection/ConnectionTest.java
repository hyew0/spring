package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionTest {

	@Test
	void driverManager() throws SQLException {
		Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		log.info("connection={}, class={}", con1, con1.getClass());
		log.info("connection={}, class={}", con2, con2.getClass());
	}

	@Test
	void dataSourceDriverManager() throws SQLException {
		//DriverManagerDataSource - 항상 새로운 커넥션 획득
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		useDataSource(dataSource);
	}
	private void useDataSource(DataSource dataSource) throws SQLException {
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();
		log.info("connection={}, class={}", con1, con1.getClass());
		log.info("connection={}, class={}", con2, con2.getClass());
	}

	@Test
	void dataSourceConnectionPool() throws SQLException, InterruptedException {
		//커넥션 풀링: HikariProxyConnection(Proxy) -> JdbcConnection(Target)
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		dataSource.setMaximumPoolSize(10);
		dataSource.setPoolName("MyPool");
		useDataSource(dataSource);
		Thread.sleep(1000); //커넥션 풀에서 커넥션 생성 시간 대기
	}
	/*
	- HikariCP 커넥션 풀을 사용한다. HikariDataSource 는 DataSource 인터페이스를 구현하고 있다.
	- 커넥션 풀 최대 사이즈를 10으로 지정하고, 풀의 이름을 MyPool 이라고 지정했다.
	- 커넥션 풀에서 커넥션을 생성하는 작업은 애플리케이션 실행 속도에 영향을 주지 않기 위해 별도의 쓰레드에서 작동한다.
	  별도의 쓰레드에서 동작하기 때문에 테스트가 먼저 종료되어 버린다.
	  예제처럼 Thread.sleep 을 통해 대기 시간을 주어야 커넥션 풀에 커넥션이 생성되는 로그를 확인할 수 있다
	  예제처럼 Thread.sleep 을 통해 대기 시간을 주어야 커넥션 풀에 커넥션이 생성되는 로그를 확인할 수 있다
	* */
}
