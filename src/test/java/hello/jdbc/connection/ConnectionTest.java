package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
}
