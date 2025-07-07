package hello.jdbc.exception.translator;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringExceptionTranslatorTest {

	DataSource dataSource;

	@BeforeEach
	void init() {
		dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
	}

	@Test
	void sqlExceptionErrorCode() {
		String sql = "select bad grammar";

		try {
			Connection con = dataSource.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.executeQuery();
		} catch (SQLException e) {
			assertThat(e.getErrorCode()).isEqualTo(42122);
			int errorCode = e.getErrorCode();
			log.info("errorCode={}", errorCode);
			//org.h2.jdbc.JdbcSQLSyntaxErrorException
			log.info("error", e);
		}
	}

	@Test
	void exceptionTranslator() {
		String sql = "select bad grammar";

		try {
			Connection con = dataSource.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.executeQuery();
		} catch (SQLException e) {
			assertThat(e.getErrorCode()).isEqualTo(42122);
			//org.springframework.jdbc.support.sql-error-codes.xml

			SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
			//org.springframework.jdbc.BadSqlGrammarException
			DataAccessException resultEx = exTranslator.translate("select", sql, e);
			log.info("resultEx", resultEx);

			assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
		}
	}
}
/*
- translate() 메서드의 첫번째 파라미터는 읽을 수 있는 설명이고, 두번째는 실행한 sql, 마지막은 발생된 SQLException 을 전달.
	-  이렇게 하면 적절한 스프링 데이터 접근 계층의 예외로 변환해서 반환해줌.
* */
