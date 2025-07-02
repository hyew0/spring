# DB1 연습 브랜치

# H2 데이터베이스 설정
- H2 데이터베이스는 개발이나 테스트 용도로 사용하기 좋은 가볍고 편리한 DB이다. 
  - 그리고 SQL을 실행할 수 있는 웹 화면을 제공한다.

- 다운 링크
  - https://www.h2database.com
  - 다운로드 및 설치
    - h2 데이터베이스 버전은 스프링 부트 버전에 맞춘다.
      - 스프링 부트 2.x를 사용하면 1.4.200 버전을 다운로드 받으면 된다.
      - 스프링 부트 3.x를 사용하면 2.1.214 버전 이상 사용해야 한다.
      - 다음 링크에 가면 다양한 H2 다운로드 버전을 확인할 수 있다.
      - https://www.h2database.com/html/download-archive.html

- MAC, 리눅스 사용자
  - 디렉토리 이동 : cd bin
  - 권한 주기: chmod 755 h2.sh
  - 실행: ./h2.sh
- 윈도우 사용자
  - 실행: h2.bat
- 데이터베이스 파일 생성 방법
  - 사용자명은 sa 입력
  - JDBC URL에 다음 입력,
  - jdbc:h2:~/test (최초 한번) 이 경우 연결 시험 을 호출하면 오류가 발생한다. 연결 을 직접 눌러주어야 한다.
  - ~/test.mv.db 파일 생성 확인
  - 이후부터는 jdbc:h2:tcp://localhost/~/test 이렇게 접속

# 1.JDBC 이해

### JDBC 등장 이유
- 애플리케이션을 개발할 때 중요한 데이터는 대부분 DB에 보관.

- 애플리케이션 서버와 DB - 일반적인 사용법
  - 커넥션 연결
    - 주로 TCP/IP를 사용해서 커넥션을 연결한다.
  - SQL 전달
    - 애플리케이션 서버는 DB가 이해할 수 있는 SQL을 연결된 커넥션을 통해 DB에 전달한다.
  - 결과 응답
    - DB는 전달된 SQL을 수행하고 그 결과를 응답한다. 애플리케이션 서버는 응답 결과를 활용한다.

- 문제점
  - 각각의 데이터베이스마다 커넥션을 연결하는 방법 같은 사용법 상에서의 방법이 모두 다르다는 문제 발생.
  - 관계형 데이터베이스는 수십개나 된다.
  - 그렇게 되면 다음과 같은 2가지 문제 발생
    - 데이터베이스를 다른 종류의 데이터베이스로 변경하면 애플리케이션 서버에 개발된 데이터베이스 사용 코드도 함께 변경해야 한다. 
    - 개발자가 각각의 데이터베이스마다 커넥션 연결, SQL 전달, 그리고 그 결과를 응답 받는 방법을 새로 학습해야 한다.

- 해결 방법으로 JDBC라는 자바 표준이 등장하게 되었다.

### JDBC 표준 인터페이스
- JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API다. 
  - JDBC 는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공한다

- 대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서 제공한다.
  - java.sql.Connection - 연결
  - java.sql.Statement - SQL을 담은 내용
  - java.sql.ResultSet - SQL 요청 응답

- 자바는 이렇게 표준 인터페이스를 정의해두었다. 
  - 이제부터 개발자는 이 표준 인터페이스만 사용해서 개발하면 된다.
  - 그런데 인터페이스만 있다고해서 기능이 동작하지는 않는다. 
  - 이 JDBC 인터페이스를 각각의 DB 벤더(회사)에서 자신의 DB에 맞도록 구현해서 라이브러리로 제공하는데, 이것을 JDBC 드라이버라 한다. 
    - 예를 들어서 MySQL DB에 접근할 수 있는 것은 MySQL JDBC 드라이버라 하고, Oracle DB에 접근할 수 있는 것은 Oracle JDBC 드라이버라 한다.

- 정리
  - JDBC의 등장으로 다음 2가지 문제 해결.
    - 데이터베이스를 다른 종류의 데이터베이스로 변경하면 애플리케이션 서버의 데이터베이스 사용 코드도 함께 변경 해야하는 문제
      - 애플리케이션 로직은 이제 JDBC 표준 인터페이스에만 의존한다. 
        - 따라서 데이터베이스를 다른 종류의 데이터베이스로 변경하고 싶으면 JDBC 구현 라이브러리만 변경하면 된다. 
        - 따라서 다른 종류의 데이터베이스로 변경해도 애플리케이션 서버의 사용 코드를 그대로 유지할 수 있다.
    - 개발자가 각각의 데이터베이스마다 커넥션 연결, SQL 전달, 그리고 그 결과를 응답 받는 방법을 새로 학습해야하는 문제
      - 개발자는 JDBC 표준 인터페이스 사용법만 학습하면 된다. 
        - 한번 배워두면 수십개의 데이터베이스에 모두 동일하게 적용할 수 있다.

- 참고 - 표준화의 한계
  - JDBC의 등장으로 많은 것이 편리해졌지만, 각각의 데이터베이스마다 SQL, 데이터타입 등의 일부 사용법 다르다. 
  - 대표적으로 실무에서 기본으로 사용하는 페이징 SQL은 각각의 데이터베이스마다 사용법이 다르다.
  - 결국 데이터베이스를 변경하면 JDBC 코드는 변경하지 않아도 되지만 SQL은 해당 데이터베이스에 맞도록 변경해야한다.
  - 참고로 JPA(Java Persistence API)를 사용하면 이렇게 각각의 데이터베이스마다 다른 SQL을 정의해야 하는 문제도 많은 부분 해결할 수 있다.

## JDBC와 최신 데이터 접근 기술
- JDBC는 1997년에 출시된 오래된 기술이며 사용법도 복잡하다.
  - 그래서 최근에는 JDBC를 직접 사용하는 것이 아니라 JDBC를 편리하게 사용하는 다양한 기술이 존재한다.
    - 대표적으로 SQL Mapper 와 ORM 기술로 나눌 수 있다.

#### SQL Mapper
- 장점: 
  - JDBC를 편리하게 사용하도록 도와준다.
    - SQL 응답 결과를 객체로 편리하게 변환해준다.
    - JDBC의 반복 코드를 제거해준다.
- 단점: 
  - 개발자가 SQL을 직접 작성해야한다.
- 대표 기술: 
  - 스프링 JdbcTemplate, MyBatis

#### ORM 기술
- ORM은 객체를 관계형 데이터베이스 테이블과 매핑해주는 기술이다. 
  - 이 기술 덕분에 개발자는 반복적인 SQL을 직접 작성하지 않고, ORM 기술이 개발자 대신에 SQL을 동적으로 만들어 실행해준다. 
  - 추가로 각각의 데이터베이스마다 다른 SQL을 사용하는 문제도 중간에서 해결해준다.
- 대표 기술: 
  - JPA, 하이버네이트, 이클립스링크
- JPA는 자바 진영의 ORM 표준 인터페이스이고, 이것을 구현한 것으로 하이버네이트와 이클립스 링크 등의 구현 기술이 있다.
  - JPA 구현체
    - 하이버네이트
    - 이클립스링크

#### SQL Mapper vs ORM 기술
- SQL Mapper와 ORM 기술 둘다 각각 장단점이 있다.
  - 쉽게 설명하자면 SQL Mapper는 SQL만 직접 작성하면 나머지 번거로운 일은 SQL Mapper가 대신 해결해준다.
    - SQL Mapper는 SQL만 작성할 줄 알면 금방 배워서 사용할 수 있다.
  - ORM기술은 SQL 자체를 작성하지 않아도 되어서 개발 생산성이 매우 높아진다. 
    - 편리한 반면에 쉬운 기술은 아니므로 실무에서 사용하려면 깊이있게 학습해야 한다

- 중요
  - 이런 기술들도 내부에서는 모두 JDBC를 사용한다. 
  - 따라서 JDBC를 직접 사용하지는 않더라도, JDBC가 어떻게 동작하는지 기본 원리를 알아두어야 한다. 
  - 그래야 해당 기술들을 더 깊이있게 이해할 수 있고, 무엇보다 문제가 발생했을 때 근본적인 문제를 찾아서 해결할 수 있다.
  - JDBC는 자바 개발자라면 꼭 알아두어야 하는 필수 기본 기술 이다.

## 데이터베이스 연결

- 데이터베이스에 연결하려면 JDBC가 제공하는 DriverManager.getConnection(..) 를 사용하면 된다. 
  - 이렇게 하면 라이브러리에 있는 데이터베이스 드라이버를 찾아서 해당 드라이버가 제공하는 커넥션을 반환해준다.

- JDBC 커넥션 인터페이스와 구현
  - JDBC는 java.sql.Connection 표준 커넥션 인터페이스를 정의한다.
  - H2 데이터베이스 드라이버는 JDBC Connection 인터페이스를 구현한 org.h2.jdbc.JdbcConnection 구현체를 제공한다.

- DriverManager 커넥션 요청 흐름
  - JDBC가 제공하는 DriverManager 는 라이브러리에 등록된 DB 드라이버들을 관리하고, 커넥션을 획득하는 기능을 제공한다.
    - 애플리케이션 로직에서 커넥션이 필요하면 DriverManager.getConnection() 을 호출한다.
    - DriverManager 는 라이브러리에 등록된 드라이버 목록을 자동으로 인식한다. 이 드라이버들에게 순서대로 다음 정보를 넘겨서 커넥션을 획득할 수 있는지 확인한다.
      - URL: 예) jdbc:h2:tcp://localhost/~/test
      - 이름, 비밀번호 등 접속에 필요한 추가 정보
      - 여기서 각각의 드라이버는 URL 정보를 체크해서 본인이 처리할 수 있는 요청인지 확인한다. 
        - 예를 들어서 URL이 jdbc:h2 로 시작하면 이것은 h2 데이터베이스에 접근하기 위한 규칙이다. 
        - 따라서 H2 드라이버는 본인이 처리할 수 있으므로 실제 데이터베이스에 연결해서 커넥션을 획득하고 이 커넥션을 클라이언트에 반환한다. 
        - 반면에 URL이 jdbc:h2 로 시작했는데 MySQL 드라이버가 먼저 실행되면 이 경우 본인이 처리할 수 없다는 결과를 반환하게 되고, 다음 드라이버에게 순서가 넘어간다.
    - 이렇게 찾은 커넥션 구현체가 클라이언트에 반환된다.

## JDBC 개발 - 등록
- con.prepareStatement(sql) 
  - 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비한다.
    - sql 
      - insert into member(member_id, money) values(?, ?)"
    - pstmt.setString(1, member.getMemberId()) 
      - SQL의 첫번째 ? 에 값을 지정한다. 문자이므로 setString 을 사용한다.
    - pstmt.setInt(2, member.getMoney()) 
      - SQL의 두번째 ? 에 값을 지정한다. Int 형 숫자이므로 setInt 를 지정한다.
    - pstmt.executeUpdate() 
      - Statement 를 통해 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달한다. 
      - 참고로 executeUpdate() 은 int 를 반환하는데 영향받은 DB row 수를 반환한다. 
      - 예시에서는 하나의 row 를 등록했으므로 1을 반환한다.

- 리소스 정리
  - 쿼리를 실행하고 나면 리소스를 정리해야 한다. 
  - 여기서는 Connection , PreparedStatement 를 사용했다. 
  - 리소스를 정리할 때는 항상 역순으로 해야한다. 
  - Connection 을 먼저 획득하고 Connection 을 통해 PreparedStatement 를 만들었기 때문에 
    - 리소스를 반환할 때는 PreparedStatement 를 먼저 종료하고, 그 다음에 Connection 을 종료하면 된다. 
    - 참고로 현재까지 설명하지 않은 ResultSet 은 결과를 조회할 때 사용한다.

  - 주의
    - 리소스 정리는 꼭! 해주어야 한다. 
    - 따라서 예외가 발생하든, 하지 않든 항상 수행되어야 하므로 finally 구문에 주의해서 작성해야한다. 
    - 만약 이 부분을 놓치게 되면 커넥션이 끊어지지 않고 계속 유지되는 문제가 발생할 수 있다. 
    - 이런 것을 리소스 누수라고 하는데, 결과적으로 커넥션 부족으로 장애가 발생할 수 있다.

- 참고
  - PreparedStatement 는 Statement 의 자식 타입인데, ? 를 통한 파라미터 바인딩을 가능하게 해준다.
  - 참고로 SQL Injection 공격을 예방하려면 PreparedStatement 를 통한 파라미터 바인딩 방식을 사용해야 한다.
  - 그냥 PreparedStatement 를 사용하자!

## JDBC 개발 - 조회
- rs = pstmt.executeQuery() 
  - 데이터를 변경할 때는 executeUpdate() 를 사용하지만, 데이터를 조회할 때는 executeQuery() 를 사용한다.
  - executeQuery() 는 결과를 ResultSet 에 담아서 반환한다.

#### ResultSet
- rs.next()  
  - 이것을 호출하면 커서가 다음으로 이동한다. 
  - 참고로 최초의 커서는 데이터를 가리키고 있지 않기 때문에 rs.next() 를 최초 한번은 호출해야 데이터를 조회할 수 있다.
    - rs.next() 의 결과가 true 면 커서의 이동 결과 데이터가 있다는 뜻이다.
    - rs.next() 의 결과가 false 면 더이상 커서가 가리키는 데이터가 없다는 뜻이다.
- rs.getString("member_id") 
  - 현재 커서가 가리키고 있는 위치의 member_id 데이터를 String 타입으로 반환한다.
- rs.getInt("money") 
  - 현재 커서가 가리키고 있는 위치의 money 데이터를 int 타입으로 반환한다.


## JDBC 개발 - 수정, 삭제
- 수정과 삭제는 등록과 비슷하다. 
  - 등록, 수정, 삭제처럼 데이터를 변경하는 쿼리는 executeUpdate() 를 사용하면 된다.

- 참고
  - 마지막에 회원을 삭제하기 때문에 테스트가 정상 수행되면, 이제부터는 같은 테스트를 반복해서 실행할 수 있다.
  - 물론 테스트 중간에 오류가 발생해서 삭제 로직을 수행할 수 없다면 테스트를 반복해서 실행할 수 없다.
  - 트랜잭션을 활용하면 이 문제를 깔끔하게 해결할 수 있는데, 자세한 내용은 트랜잭션 부분에서 정리.


# 2.커넥션풀과 데이터소스 이해

## 커넥션 풀 이해
- 커넥션 풀은 커넥션을 미리 만들어서 보관해두고, 필요할 때마다 꺼내서 사용하는 기술이다.
  - 커넥션을 매번 새로 만드는 것은 비용이 크기 때문에, 커넥션 풀을 사용하면 성능을 크게 향상시킬 수 있다.

- 적절한 커넥션 풀 숫자는 서비스의 특징과 애플리케이션 서버 스펙, DB 서버 스펙에 따라 다르기 때문에 성능 테스트를 통해서 정해야 한다.
- 커넥션 풀은 서버당 최대 커넥션 수를 제한할 수 있다. 따라서 DB에 무한정 연결이 생성되는 것을 막아주어서 DB를 보호하는 효과도 있다.
- 이런 커넥션 풀은 얻는 이점이 매우 크기 때문에 실무에서는 항상 기본으로 사용한다.
- 커넥션 풀은 개념적으로 단순해서 직접 구현할 수도 있지만, 사용도 편리하고 성능도 뛰어난 오픈소스 커넥션 풀이 많기 때문에 오픈소스를 사용하는 것이 좋다.
  - 대표적인 커넥션 풀 오픈소스는 commons-dbcp2 , tomcat-jdbc pool , HikariCP 등이 있다.
- 성능과 사용의 편리함 측면에서 최근에는 hikariCP 를 주로 사용한다. 
  - 스프링 부트 2.0 부터는 기본 커넥션 풀로 hikariCP 를 제공한다. 성능, 사용의 편리함, 안전성 측면에서 이미 검증이 되었기 때문에 커넥션 풀을 사용할 때는 고민할 것 없이 hikariCP 를 사용하면 된다. 
  - 실무에서도 레거시 프로젝트가 아닌 이상 대부분 hikariCP 를 사용한다.

## DataSource 이해
- 커넥션을 획득하는 다양한 방법
  - JDBC는 DriverManager.getConnection() 을 통해서 커넥션을 획득할 수 있다.
  - DBCP2, HikariCP 등 커넥션 풀을 사용하면 DataSource 를 통해서 커넥션을 획득할 수 있다.
    - 다음과 같이 DriverManager을 통해 커넥션을 획득하다가 커넥션 풀을 사용하는 방법으로 변경하면 애플리케이션 코드로 함께 변경해야 한다.
      - 자바에서는 이 문제를 해결하기 위해 javax.sql.DataSource 인터페이스를 제공한다.
      - DataSource는 커넥션을 획득하는 방법을 추상화하는 인터페이스다.
        - 이 인터페이스의 핵심 기능은 커넥션 조회 하나이다. (다른 일부 기능도 있지만 크게 중요하지 않다.)
- ```java
  public interface DataSource {
      Connection getConnection() throws SQLException;
  }
  ```
- 정리
  - 대부분의 커넥션 풀은 DataSource 인터페이스를 이미 구현해두었다. 
    - 따라서 개발자는 DBCP2 커넥션 풀 , HikariCP 커넥션 풀 의 코드를 직접 의존하는 것이 아니라 DataSource 인터페이스에만 의존하도록 애플리케이션 로직을 작성하면 된다.
  - 커넥션 풀 구현 기술을 변경하고 싶으면 해당 구현체로 갈아끼우기만 하면 된다.
  - DriverManager 는 DataSource 인터페이스를 사용하지 않는다. 
    - 따라서 DriverManager 는 직접 사용해야 한다. 
    - 따라서 DriverManager 를 사용하다가 DataSource 기반의 커넥션 풀을 사용하도록 변경하면 관련 코드를 다 고쳐야 한다. 
    - 이런 문제를 해결하기 위해 스프링은 DriverManager 도 DataSource 를 통해서 사용할 수 있도록 DriverManagerDataSource 라는 DataSource 를 구현한 클래스를 제공한다.
  - 자바는 DataSource 를 통해 커넥션을 획득하는 방법을 추상화했다. 
    - 이제 애플리케이션 로직은 DataSource 인터페이스에만 의존하면 된다. 
    - 덕분에 DriverManagerDataSource 를 통해서 DriverManager 를 사용 하다가 커넥션 풀을 사용하도록 코드를 변경해도 애플리케이션 로직은 변경하지 않아도 된다. 

## DataSource - DriverManager
- 파라미터 차이
  - 기존 DriverManager 를 통해서 커넥션을 획득하는 방법과 DataSource 를 통해서 커넥션을 획득하는 방법에는 큰 차이가 있다.
- DriverManager
  - ```
    DriverManager.getConnection(URL, USERNAME, PASSWORD)
    DriverManager.getConnection(URL, USERNAME, PASSWORD)
    ```
- DataSource
  - ```java
    void dataSourceDriverManager() throws SQLException {
      DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME, PASSWORD);
      useDataSource(dataSource);
    }
    private void useDataSource(DataSource dataSource) throws SQLException {
      Connection con1 = dataSource.getConnection();
      Connection con2 = dataSource.getConnection();
      log.info("connection={}, class={}", con1, con1.getClass());
      log.info("connection={}, class={}", con2, con2.getClass());
    }
    ```
- DriverManager 는 커넥션을 획득할 때 마다 URL , USERNAME , PASSWORD 같은 파라미터를 계속 전달해야 한다. 
  - 반면에 DataSource 를 사용하는 방식은 처음 객체를 생성할 때만 필요한 파리미터를 넘겨두고, 커넥션을 획득할 때는 단순히 dataSource.getConnection() 만 호출하면 된다.
- 설정과 사용의 분리
  - 설정: DataSource 를 만들고 필요한 속성들을 사용해서 URL , USERNAME , PASSWORD 같은 부분을 입력하는 것을 말한다. 
    - 이렇게 설정과 관련된 속성들은 한 곳에 있는 것이 향후 변경에 더 유연하게 대처할 수 있다.
  - 사용: 설정은 신경쓰지 않고, DataSource 의 getConnection() 만 호출해서 사용하면 된다.
- 설정과 사용의 분리 설명
  - 이 부분이 작아보이지만 큰 차이를 만들어내는데, 
    - 필요한 데이터를 DataSource 가 만들어지는 시점에 미리 다 넣어두게 되면, 
    - DataSource 를 사용하는 곳에서는 dataSource.getConnection() 만 호출하면 되므로, URL , USERNAME , PASSWORD 같은 속성들에 의존하지 않아도 된다. 
    - 그냥 DataSource 만 주입받아서 getConnection() 만 호출하면 된다.
  - 쉽게 이야기해서 리포지토리(Repository)는 DataSource 만 의존하고, 이런 속성을 몰라도 된다.
  - 애플리케이션을 개발해보면 보통 설정은 한 곳에서 하지만, 사용은 수 많은 곳에서 하게 된다.
  - 덕분에 객체를 설정하는 부분과, 사용하는 부분을 좀 더 명확하게 분리할 수 있다.

## DataSource - 커넥션 풀
- HikariConfig
  - HikariCP 관련 설정을 확인할 수 있다. 
  - 풀의 이름( MyPool )과 최대 풀 수( 10 )을 확인할 수 있다.
- MyPool connection adder
  - 별도의 쓰레드 사용해서 커넥션 풀에 커넥션을 채우고 있는 것을 확인할 수 있다. 
  - 이 쓰레드는 커넥션 풀에 커넥션을 최대 풀 수( 10 )까지 채운다.
  - 그렇다면 왜 별도의 쓰레드를 사용해서 커넥션 풀에 커넥션을 채우는 것일까?
    - 커넥션 풀에 커넥션을 채우는 것은 상대적으로 오래 걸리는 일이다. 
    - 애플리케이션을 실행할 때 커넥션 풀을 채울 때 까지 마냥 대기하고 있다면 애플리케이션 실행 시간이 늦어진다. 
    - 따라서 이렇게 별도의 쓰레드를 사용해서 커넥션 풀을 채워야 애플리케이션 실행 시간에 영향을 주지 않는다.
- 참고
  - HikariCP 커넥션 풀에 대한 더 자세한 내용은 다음 공식 사이트를 참고하자.
  - https://github.com/brettwooldridge/HikariCP

#### 스프링 부트 3.1 이상 - 로그 출력 안되는 문제 해결
- 히카리 커넥션 풀을 테스트하는 dataSourceConnectionPool() 을 실행할 때, 
  - 스프링 부트 3.1 이상을 사용한다면 전체 로그가 아니라 다음과 같은 간략한 로그만 출력된다.
  - 스프링 부트 3.1 이상
  - ```
    14:14:55.926 [Test worker] INFO com.zaxxer.hikari.HikariDataSource -- MyPool -
    Starting...
    14:14:55.993 [Test worker] INFO com.zaxxer.hikari.pool.HikariPool -- MyPool -
    Added connection conn0: url=jdbc:h2:tcp://localhost/~/test user=SA
    14:14:55.994 [Test worker] INFO com.zaxxer.hikari.HikariDataSource -- MyPool -
    Start completed.
    14:14:55.997 [Test worker] INFO hello.jdbc.connection.ConnectionTest --
    connection=HikariProxyConnection@2066892165 wrapping conn0: url=jdbc:h2:tcp://
    localhost/~/test user=SA, class=class
    com.zaxxer.hikari.pool.HikariProxyConnection
    14:14:56.000 [Test worker] INFO hello.jdbc.connection.ConnectionTest --
    connection=HikariProxyConnection@276869158 wrapping conn1: url=jdbc:h2:tcp://
    localhost/~/test user=SA, class=class
    com.zaxxer.hikari.pool.HikariProxyConnection
      ```
  - 이때는 다음 위치에 파일을 만들어서 넣으면 된다.
    - src/main/resources/logback.xml
    ```xml
    <configuration>
     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     <encoder>
     <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} -%kvp-%msg%n</pattern>
     </encoder>
     </appender>
     <root level="DEBUG">
     <appender-ref ref="STDOUT" />
     </root>
    </configuration>
    ```
    - 스프링 부트 3.1 부터 기본 로그 레벨을 INFO 로 빠르게 설정하기 때문에 로그를 확인할 수 없는데, 
      - 이렇게하면 기본 로그 레벨을 DEBUG 로 설정해서 강의 내용과 같이 로그를 확인할 수 있다.

## DataSource 적용

# 3. 트랜잭션 이해

## 트랜잭션 - 개념 이해
- 데이터를 파일에 저장하는게 아니라 데이터베이스에 저장하는 이유는 무엇일까?
  - 가장 대표적인 이유로 데이터베이스가 트랜잭션이라는 개념을 지원하기 때문.
- 트랜잭션을 이름 그대로 번역하면 -> "거래"
  - 트랜잭션은 하나의 거래를 안전하게 처리하도록 보장해주는 것을 뜻한다.
  - 예를 들어서 은행에서 돈을 이체하는 경우를 생각해보자.
    - A 계좌에서 B 계좌로 돈을 이체하는 경우, A 계좌에서 돈이 빠져나가고 B 계좌에 돈이 들어가는 2가지 작업이 필요하다.
    - 그런데 만약 A 계좌에서 돈이 빠져나갔는데, B 계좌에 돈이 들어가지 않는다면 어떻게 될까?
      - A 계좌는 돈이 빠져나갔는데, B 계좌는 돈이 들어오지 않아서 데이터가 불일치하게 된다. 
      - 이런 상황은 절대 발생해서는 안된다.
  - 트랜잭션을 사용하면 이런 문제를 해결 가능하다.
    - 트랜잭션은 A 계좌에서 돈이 빠져나가고 B 계좌에 돈이 들어가는 2가지 작업을 하나의 트랜잭션으로 묶어서 처리한다.
      - 모든 작업이 성공해서 데이터베이스에 정상 반영하는 것을 커밋(Commit).
      - 작업 중 하나라도 실패해서 거래 이전으로 되돌리는 것을 롤백(Rallback)이라 한다.
### 트랜잭션 ACID
- 트랜잭션은 ACID(http://en.wikipedia.org/wiki/ACID)라 하는 원자성(Atomicity), 일관성(Consistency), 격리성(Isolation), 지속성(Durability)을 보장해야 한다.
  - 원자성: 
    - 트랜잭션 내에서 실행한 작업들은 마치 하나의 작업인 것처럼 모두 성공 하거나 모두 실패해야 한다.
  - 일관성: 
    - 모든 트랜잭션은 일관성 있는 데이터베이스 상태를 유지해야 한다. 
    - 예를 들어 데이터베이스에서 정한 무결성 제약 조건을 항상 만족해야 한다.
  - 격리성: 
    - 동시에 실행되는 트랜잭션들이 서로에게 영향을 미치지 않도록 격리한다. 
    - 예를 들어 동시에 같은 데이터를 수정하지 못하도록 해야 한다. 
    - 격리성은 동시성과 관련된 성능 이슈로 인해 트랜잭션 격리 수준(Isolation level)을 선택할 수 있다.
  - 지속성: 
    - 트랜잭션을 성공적으로 끝내면 그 결과가 항상 기록되어야 한다. 
    - 중간에 시스템에 문제가 발생해도 데이터베이스 로그 등을 사용해서 성공한 트랜잭션 내용을 복구해야 한다.
- 트랜잭션은 원자성, 일관성, 지속성을 보장한다. 
  - 문제는 격리성이다. 
    - 트랜잭션 격리성은 동시성 문제와 관련이 있다.
    - 격리성을 완전히 보장하려면 트랜잭션을 거의 순서대로 실행해야 하고, 이 과정에서 동시 처리 성능이 나빠질 수 밖에 없다.
    - 이런 문제로 인해 ANSI 표준은 트랜잭션의 격리 수준을 4단계로 나누어 정리했다.

#### 트랜잭션 격리 수준 - Isolation Level
- READ UNCOMMITED(커밋되지 않은 읽기)
- READ COMMITTED(커밋된 읽기) - 일반적으로 많이 사용.
- REPEATABLE READ(반복 가능한 읽기)
- SERIALIZABLE(직렬화 가능) 

## 데이터베이스 연결 구조와 DB 세션
- 트랜잭션을 더 잘 이해하기 위해 데이터베이스 연결 구조와 DB 세션에 대해서 알아보자.
- 데이터베이스 연결 구조
  - 사용자는 웹 애플리케이션 서버(WAS)나 DB 접근 툴 같은 클라이언트를 사용해서 DB 서버에 접근 가능.
    - 클라이언트는 DB 서버에 연결 요청하고 커넥션을 맺게 된다.
    - 이때 DB 서버는 내부에 "세션"이라는 것을 만든다.
      - 그리고 앞으로 해당 커넥션을 통한 모든 요청은 이 세션을 통해서 실행하게 된다.
  - 쉽게 얘기하면, 개발자가 클라이언트를 통해 SQL을 전달하면 현재 커넥션에 연결된 세션이 SQL을 실행한다.
  - 세션은 트랜잭션을 시작하고, 커밋 또는 롤백을 통해 트랜잭션을 종료한다.
    - 그리고 이후에 새로운 트랜잭션을 다시 시작할 수 있다.
  - 사용자가 커넥션을 닫거나, 또는 DBA(Database Administrator:DB 관리자)가 세션을 강제 종료하면 세션도 종료된다.

## 트랜잭션 -DB 예제
- 트랜잭션의 실제 구현 방식은 데이터베이스마다 다르다.
- 트랜잭션 사용법
  - 데이터 변경 쿼리를 실행하고 데이터베이스에 그 결과를 반영하려면 커밋 명령어인 commit 을 호출하고, 
    - 결과를 반영하고 싶지 않으면 롤백 명령어인 rollback 을 호출하면 된다.
  - 커밋을 호출하기 전까지는 임시로 데이터를 저장하는 것이다. 
    - 따라서 해당 트랜잭션을 시작한 세션(사용자)에게만 변경 데이터가 보이고 다른 세션(사용자)에게는 변경 데이터가 보이지 않는다.
  - 등록, 수정, 삭제 모두 같은 원리로 동작한다. 
    - 앞으로는 등록, 수정, 삭제를 간단히 변경이라는 단어로 표현하겠다.

### 자동 커밋, 수동 커밋
- 자동 커밋
  - 자동 커밋으로 설정하면 각각의 쿼리 실해 직후에 자동으로 커밋을 호출한다.
  - 따라서 커밋이나 롤백을 직접 호출하지 않아도 되는 편리함이 있다. 
  - 하지만 쿼리를 하나하나 실행할 때마다 자동으로 커밋이 되어버리기 때문에 우리가 원하는 트랜잭셔 기능을 제대로 사용할 수 없다.
  - 따라서 commit, rollback 을 직접 호출해서 트랜잭션을 제어할 수 있는 수동 커밋을 사용하는 것이 좋다.
  - ```
    set autocommit true; //자동 커밋 모드 설정
    insert into member(member_id, money) values ('data1',10000); //자동 커밋
    insert into member(member_id, money) values ('data2',10000); //자동 커밋
    ```
- 수동 커밋
  - ```
    set autocommit false; //수동 커밋 모드 설정
    insert into member(member_id, money) values ('data3',10000);
    insert into member(member_id, money) values ('data4',10000);
    commit; //수동 커밋
    ```
  - 보통 자동 커밋 모드가 기본으로 설정된 경우가 많기 때문에, 수동 커밋모드로 설정하는 것을 "트랜잭션을 시작한다"라고 표현할 수 있다.
  - 수동 커밋 설정을 하면 이후에 꼭 commit, rollback 을 호출해서 트랜잭션을 종료해야 한다.
  - 참고로 수동 커밋 모드나 자동 커밋 모드는 한번 설정하면 해당 세션에서는 계속 유지된다. 
    - 중간에 변경하는 것은 가능하다.

### 트랜잭션 예제 - 트랜잭션 실습

- 데이터 초기화 SQL
  - ```sql
    //데이터 초기화
    set autocommit true;
    delete from member;
    insert into member(member_id, money) values ('oldId',10000); 
    ```
    - 자동 커밋 사용으로 별도로 커밋을 호출하지 않아도 된다.
- 세션1 신규 데이터 추가 SQL
  ```sql
  //트랜잭션 시작
  set autocommit false; //수동 커밋 모드
  insert into member(member_id, money) values ('newId1',10000);
  insert into member(member_id, money) values ('newId2',10000);
  ```
- 커밋
  - commit; //데이터베이스에 반영
  - 커밋하면 세션1에서 실행한 쿼리 결과가 데이터베이스에 반영된다. 
    - 그러면 세션2가 조회했을 때도 세션1에서 추가한 데이터가 보인다.

- 롤백
  - rollback; //데이터베이스에 반영하지 않음
  - 롤백하면 세션1에서 실행한 쿼리 결과가 데이터베이스에 반영되지 않는다. 
    - 그러면 세션2가 조회했을 때 세션1에서 추가한 데이터가 보이지 않는다.(당연, 세션1에서 추가한 데이터가 없으니까)

### 정리
- 원자성: 트랜잭션 내에서 실행한 작업들은 마치 하나의 작업인 것처럼 모두 성공 하거나 모두 실패해야 한다.
  - 트랜잭션의 원자성 덕분에 여러 SQL 명령어를 마치 하나의 작업인 것 처럼 처리할 수 있었다. 
  - 성공하면 한번에 반영하고, 중간에 실패해도 마치 하나의 작업을 되돌리는 것 처럼 간단히 되돌릴 수 있다.
- 오토 커밋
  - 만약 오토 커밋 모드로 동작하는데, 계좌이체 중간에 실패하면 어떻게 될까? 
  - 쿼리를 하나 실행할 때 마다 바로바로 커밋이 되어버리기 때문에 memberA 의 돈만 2000원 줄어드는 심각한 문제가 발생한다.
- 트랜잭션 시작
  - 따라서 이런 종류의 작업은 꼭 수동 커밋 모드를 사용해서 수동으로 커밋, 롤백 할 수 있도록 해야 한다. 
  - 보통 이렇게 자동 커밋 모드에서 수동 커밋 모드로 전환 하는 것을 트랜잭션을 시작한다고 표현한다.

## DB 락 - 개념 이해
- 트랜잭션을 시작하고 커밋을 수행하지 않았을 때, 동시에 같은 데이터를 수정하면 트랜잭션의 원자성이 깨지게 된다.
  - 이런 문제를 방지하려면, 세션이 트랜잭션을 시작하고 데이터를 수정하는 동안에는 커밋이나 롤백 전까지 다른 세션에서 해당 데이터를 수정할 수 없게 막아야 한다.
- 하나의 세션이 트랜잭션을 시작할 때 락을 획득하여 작업을 실행하는 동안 다른 세션은 락을 획득할 수 없으므로 대기한다.
  - 락을 무한정 대기하는 것이 아니라, 락 대기 시간을 넘어가면 락 타임아웃 오류가 발생한다. 
    - 락 대기 시간은 설정할 수 있다.

### 실습 코드
- 기본 데이터 입력 - SQL
  - ```sql
  set autocommit true;
  delete from member;
  insert into member(member_id, money) values ('memberA',10000);
  ```
- 세션1
```sql
set autocommit false;
update member set money=500 where member_id = 'memberA';
```
  - 세션1이 트랜잭션을 시작하고, memberA 의 데이터를 500원으로 업데이트 했다. 아직 커밋은 하지 않았다.
    - memberA 로우의 락은 세션1이 가지게 된다.  
- 세션2
  ```sql
  SET LOCK_TIMEOUT 60000;
  set autocommit false;
  update member set money=1000 where member_id = 'memberA';
  ```
  - 세션2는 memberA 의 데이터를 1000원으로 수정하려 한다.
  - 세션1이 트랜잭션을 커밋하거나 롤백해서 종료하지 않았으므로 아직 세션1이 락을 가지고 있다. 
    - 따라서 세션2는 락을 획득하지 못하기 때문에 데이터를 수정할 수 없다. 
    - 세션2는 락이 돌아올 때 까지 대기하게 된다.
  - SET LOCK_TIMEOUT 60000 : 락 획득 시간을 60초로 설정한다. 
    - 60초 안에 락을 얻지 못하면 예외가 발생한다.
    - 참고로 H2 데이터베이스에서는 딱 60초에 예외가 발생하지는 않고, 시간이 조금 더 걸릴 수 있다.
  - 세션2 락 획득
    - 세션1을 커밋하면 세션1이 커밋되면서 락을 반납한다. 이후에 대기하던 세션2가 락을 획득하게 된다. 
      - 따라서 락을 획득한 세션2의 업데이트가 반영되는 것을 확인할 수 있다. 
      - 물론 이후에 세션2도 커밋을 호출해서 락을 반납해야 한다.
- 세션1
  ```sql
  commit;
  ```
- 세션2
  ```sql
  commit;
  ```

#### 세션2 락 타임아웃
- SET LOCK_TIMEOUT <milliseconds> : 락 타임아웃 시간을 설정한다.
- 예) SET LOCK_TIMEOUT 10000 10초, 세션2에 설정하면 세션2가 10초 동안 대기해도 락을 얻지 못하면 락 타임아웃 오류가 발생한다.
- 위 시나리오 중간에 락을 오랜기간 대기하면 어떻게 되는지 알아보자.
  - 10초 정도 기다리면 세션2에서는 다음과 같은 락 타임아웃 오류가 발생한다.
- 세션2의 실행 결과
```
Timeout trying to lock table {0}; SQL statement:
update member set money=10000 - 2000 where member_id = 'memberA' [50200-200]
HYT00/50200
```
- 세션1이 memberA 의 데이터를 변경하고, 트랜잭션을 아직 커밋하지 않았다. 
  - 따라서 세션2는 세션1이 트랜잭션을 커밋하거나 롤백할 때 까지 대기해야 한다. 
  - 기다리면 락 타임아웃 오류가 발생하는 것을 확인할 수 있다

## DB 락 - 조회
- 일반적인 조회는 락을 사용하지 않는다
  - 데이터베이스마다 다르지만, 보통 데이터를 조회할 때는 락을 획득하지 않고 바로 데이터를 조회할 수 있다. 
  - 예를 들어서 세션1이 락을 획득하고 데이터를 변경하고 있어도, 세션2에서 데이터를 조회는 할 수 있다. 
  - 물론 세션2에서 조회가 아니라 데이터를 변경하려면 락이 필요하기 때문에 락이 돌아올 때 까지 대기해야 한다.
- 조회와 락
  - 데이터를 조회할 때도 락을 획득하고 싶을 때가 있다. 이럴 때는 select for update 구문을 사용하면 된다.
  - 이렇게 하면 세션1이 조회 시점에 락을 가져가버리기 때문에 다른 세션에서 해당 데이터를 변경할 수 없다.
  - 물론 이 경우도 트랜잭션을 커밋하면 락을 반납한다.
- 조회 시점에 락이 필요한 경우는 언제일까?
  - 트랜잭션 종료 시점까지 해당 데이터를 다른 곳에서 변경하지 못하도록 강제로 막아야 할 때 사용한다.
  - 예를 들어서 애플리케이션 로직에서 memberA 의 금액을 조회한 다음에 이 금액 정보로 애플리케이션에서 어떤 계산을 수행한다. 
    - 그런데 이 계산이 돈과 관련된 매우 중요한 계산이어서 계산을 완료할 때 까지 memberA 의 금액을 다른곳에서 변경하면 안된다. 
    - 이럴 때 조회 시점에 락을 획득하면 된다.
- select for update 
  - 구문을 사용하면 조회를 하면서 동시에 선택한 로우의 락도 획득한다.
  - 물론 락이 없다면 락을 획득할 때 까지 대기해야 한다
  - ```sql
    set autocommit false;
    select * from member where member_id = 'memberA' for update;
    ```

#### 정리
- 트랜잭션과 락은 데이터베이스마다 실제 동작하는 방식이 조금씩 다르기 때문에, 해당 데이터베이스 메뉴얼을 확인해보고, 의도한대로 동작하는지 테스트한 이후에 사용하자.

## 실습 - 적용
- 테스트 데이터 제거
  - 테스트가 끝나면 다음 테스트에 영향을 주지 않기 위해 @AfterEach 에서 테스트에 사용한 데이터를 모두 삭제한다.
    - @BeforeEach : 각각의 테스트가 수행되기 전에 실행된다.
    - @AfterEach : 각각의 테스트가 실행되고 난 이후에 실행된다.

###  DB 트랜잭션을 이용한 원자성 문제 해결
- 애플리케이션에서 트랜잭션을 어떤 계층에 걸어야 할까? 쉽게 이야기해서 트랜잭션을 어디에서 시작하고, 어디에서 커밋해야할까?
  - 트랜잭션은 비즈니스 로직이 있는 서비스 계층에서 시작해야 한다. 
    - 비즈니스 로직이 잘못되면 해당 비즈니스 로직으로 인해 문제가 되는 부분을 함께 롤백해야 하기 때문이다.
  - 그런데 트랜잭션을 시작하려면 커넥션이 필요하다. 
    - 결국 서비스 계층에서 커넥션을 만들고, 트랜잭션 커밋 이후에 커넥션을 종료해야 한다.
  - 애플리케이션에서 DB 트랜잭션을 사용하려면 트랜잭션을 사용하는 동안 같은 커넥션을 유지해야한다. 
    - 그래야 같은 세션을 사용할 수 있다.

- 애플리케이션에서 같은 커넥션을 유지하려면 어떻게 해야할까? 
  - 가장 단순한 방법은 커넥션을 파라미터로 전달해서 같은 커넥션이 사용되도록 유지하는 것이다.

- 남은 문제
  - 애플리케이션에서 DB 트랜잭션을 적용하려면 서비스 계층이 매우 지저분해지고, 생각보다 매우 복잡한 코드를 요구한다. 
  - 추가로 커넥션을 유지하도록 코드를 변경하는 것도 쉬운 일은 아니다.
  - 이후에 스프링을 사용해서 하나씩 해결해본다.

# 4.스프링과 문제 해결 - 트랜잭션

## 문제점들
- 애플리케이션 구조
  - 애플리케이션 구조는 다음과 같다.
    - 프레젠테이션 계층 -> 서비스 계층 -> 데이터 접근 계층 -> DB 서버
    - Controller     -> Service   -> Repository 
  - 프레젠테이션 계층(Controller)
    - UI와 관련된 처리 담당
    - 웹 요청과 응답
    - 사용자 요청을 검증
    - 주 사용 기술: 서블릿과 HTTP 같은 웹 기술, 스프링 MVC
  - 서비스 계층(Service)
    - 비즈니스 로직을 담당
    - 주 사용 기술: 
      - 가급적 특정 기술에 의존하지 않고, 순수 자바 코드로 작성
  - 데이터 접근 계층(Repository)
    - 실제 데이터베이스에 접근하는 코드
    - 주 사용 기술: 
      - JDBC, JPA, File, Redis, Mongo ...

### 서비스 계층
- 서비스 계층은 비즈니스 로직을 담당하여 특정 기술에 의존하지 않아야 하지만, 
  - MemberServiceV1과 MemberServiceV2는 JDBC 기술에 의존하고 있다. 
    -  javax.sql.DataSource , java.sql.Connection , java.sql.SQLException 등
      - = JDBC 기술
  - 향후 JDBC 기술을 JPA로 변경한다면, 
    - MemberServiceV1과 MemberServiceV2를 JPA 기술에 의존하도록 변경해야 한다.(서비스 코드 완전 변경 필요)
    - 이렇게 되면 핵심 비즈니스 로직과 JDBC 기술이 섞여있어서 유지 보수 어려움.

### 문제 정리
- 트랜잭션 문제
- 예외 누수 문제
- JDBC 반복 문제

#### 1.트랜잭션 문제
- JDBC 구현 기술이 서비스 계층에 누수되는 문제
  - 트랜잭션을 적용하기 위해 JDBC 구현 기술이 서비스 계층에 누수되었다.
  - 서비스 계층은 순수해야 한다. 구현 기술을 변경해도 서비스 계층 코드는 최대한 유지할 수 있어야 한다.(변화에 대응)
    - 그래서 데이터 접근 계층(Repository)에 JDBC 코드를 다 몰아두는 것이다.
    - 물론 데이터 접근 계층의 구현 기술이 변경될 수도 있으니 데이터 접근 계층은 인터페이스를 제공하는 것이 좋다.
  - 서비스 계층은 특정 기술에 종속되지 않아야 한다. 
    - 지금까지 그렇게 노력해서 데이터 접근 계층으로 JDBC 관련 코드를 모았는데, 트랜잭션을 적용하면서 결국 서비스 계층에 JDBC 구현 기술의 누수가 발생했다.
- 트랜잭션 동기화 문제
  - 같은 트랜잭션을 유지하기 위해 커넥션을 파라미터로 넘겨야 한다.
  - 이때 파생되는 문제들도 있다. 
    - 똑같은 기능도 트랜잭션용 기능과 트랜잭션을 유지하지 않아도 되는 기능으로 분리해야 한다.
- 트랜잭션 적용 반복 문제
  - 트랜잭션 적용 코드를 보면 반복이 많다. try , catch , finally ...

#### 2.예외 누수 문제
- 데이터 접근 계층의 JDBC 구현 기술 예외가 서비스 계층으로 전파된다.
- SQLException 은 체크 예외이기 때문에 데이터 접근 계층을 호출한 서비스 계층에서 해당 예외를 잡아서 처리하거나 명시적으로 throws 를 통해서 다시 밖으로 던져야한다.
- SQLException 은 JDBC 전용 기술이다. 
  - 향후 JPA나 다른 데이터 접근 기술을 사용하면, 그에 맞는 다른 예외로 변경해야 하고, 결국 서비스 코드도 수정해야 한다.

#### 3.JDBC 반복 문제
- 지금까지 작성한 MemberRepository 코드는 순수한 JDBC를 사용했다.
- 이 코드들은 유사한 코드의 반복이 너무 많다.
  - try , catch , finally ...
  - 커넥션을 열고, PreparedStatement 를 사용하고, 결과를 매핑하고... 실행하고, 커넥션과 리소스를 정리한다.

- 이 반복되는 코드를 어떻게 해결할 수 있을까?
  - 스프링은 서비스 계층을 순수하게 유지하면서, 
    - 트랜잭션 문제, 예외 누수 문제, JDBC 반복 문제를 해결할 수 있는 방법을 제공한다.

## 트랜잭션 추상화
- 트랜잭션을 사용하는 코드는 데이터 접근 기술마다 다르다.
  - 예를 들어서 JDBC는 Connection 을 사용하고, JPA는 EntityManager 를 사용한다.
  - 이렇게 데이터 접근 기술마다 트랜잭션을 시작하고 커밋, 롤백하는 방법이 다르다.
  - 스프링은 이런 트랜잭션을 추상화해서 통일된 인터페이스를 제공한다.
    - 트랜잭션 추상화 인터페이스
    ```java
    public interface TxManager {
     begin();
     commit();
     rollback();
    }
    ```
    - 스프링은 트랜잭션 추상화 인터페이스를 제공하고, 인터페이스를 기반으로 각각의 기술에 맞는 구현체를 만든다.
      - 예를 들어서 JDBC 트랜잭션을 위한 JdbcTransactionManager, JPA 트랜잭션을 위한 JpaTransactionManager 같은 구현체가 있다.
        - JdbcTxManager : JDBC 트랜잭션 기능을 제공하는 구현체
        - JpaTxManager : JPA 트랜잭션 기능을 제공하는 구현체
    - 스프링은 트랜잭션 추상화 기술과 데이터 접근 기술에 따른 트랜잭션 구현체도 대부분 만들어 두었기 때문에 사용만 하면 된다.
  - 스프링 트랜잭션 추상화의 핵심은 PlatformTransactionManager 인터페이스이다.
    - org.springframework.transaction.PlatformTransactionManager
    - 참고
      - 스프링 5.3부터는 JDBC 트랜잭션을 관리할 때 DataSourceTransactionManager 를 상속받아서 약간의 기능을 확장한 JdbcTransactionManager 를 제공한다. 
      - 둘의 기능 차이는 크지 않으므로 같은 것으로 이해하면 된다.

### PlatformTransactionManager 인터페이스
```java
package org.springframework.transaction;

public interface PlatformTransactionManager extends TransactionManager {
  TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;
  
  void commit(TransactionStatus status) throws TransactionException;
  void rollback(TransactionStatus status) throws TransactionException;
}
```
- getTransaction() : 트랜잭션을 시작한다.
  - 이름이 getTransaction() 인 이유는 기존에 이미 진행중인 트랜잭션이 있는 경우 해당 트랜잭션에 참여할 수 있기 때문이다.
  - 참고로 트랜잭션 참여, 전파에 대한 부분은 뒤에서 설명한다. 
  - 지금은 단순히 트랜잭션을 시작하는 것으로 이해하면 된다.
- commit() : 트랜잭션을 커밋한다.
- rollback() : 트랜잭션을 롤백한다.

## 트랜잭션 동기화
- 스프링이 제공하는 트랜잭션 매니저는 크게 2가지 역할을 한다.
  - 트랜잭션 추상화
  - 리소스 동기화

### 리소스 동기화
- 트랜잭션을 유지하려면 트랜잭션의 시작부터 끝까지 같은 데이터베이스 커넥션을 유지해아한다. 
  - 결국 같은 커넥션을 동기화(맞추어 사용)하기 위해서 이전에는 파라미터로 커넥션을 전달하는 방법을 사용했다.
  - 파라미터로 커넥션을 전달하는 방법은 코드가 지저분해지는 것은 물론이고, 
  - 커넥션을 넘기는 메서드와 넘기지 않는 메서드를 중복해서 만들어야 하는 등 여러가지 단점들이 많다.
- 스프링은 "트랜잭션 동기화 매니저"를 제공한다.
  - 이것은 쓰레드 로컬( ThreadLocal )을 사용해서 커넥션을 동기화해준다. 
  - 트랜잭션 매니저는 내부에서 이 트랜잭션 동기화 매니저를 사용한다
- 트랜잭션 동기화 매니저는 쓰레드 로컬을 사용하기 때문에 멀티쓰레드 상황에 안전하게 커넥션을 동기화 할 수 있다. 
  - 따라서 커넥션이 필요하면 트랜잭션 동기화 매니저를 통해 커넥션을 획득하면 된다. 
  - 따라서 이전처럼 파라미터로 커넥션을 전달하지 않아도 된다.

#### 동작 방식을 간단하게 설명하면 다음과 같다.
1. 트랜잭션을 시작하려면 커넥션이 필요하다. 트랜잭션 매니저는 데이터소스를 통해 커넥션을 만들고 트랜잭션을 시작한다.
2. 트랜잭션 매니저는 트랜잭션이 시작된 커넥션을 트랜잭션 동기화 매니저에 보관한다.
3. 리포지토리는 트랜잭션 동기화 매니저에 보관된 커넥션을 꺼내서 사용한다. 따라서 파라미터로 커넥션을 전달하지 않아도 된다.
4. 트랜잭션이 종료되면 트랜잭션 매니저는 트랜잭션 동기화 매니저에 보관된 커넥션을 통해 트랜잭션을 종료하고, 커넥션도 닫는다.

- 트랜잭션 동기화 매니저
  - 다음 트랜잭션 동기화 매니저 클래스를 열어보면 쓰레드 로컬을 사용하는 것을 확인할 수 있다.
  - org.springframework.transaction.support.TransactionSynchronizationManager

#### DataSourceUtils.getConnection()
- DataSourceUtils.getConnection() 는 다음과 같이 동작한다.
  - 트랜잭션 동기화 매니저가 관리하는 커넥션이 있으면 해당 커넥션을 반환한다.
  - 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 새로운 커넥션을 생성해서 반환한다.

#### DataSourceUtils.releaseConnection()
- 커넥션을 con.close() 를 사용해서 직접 닫아버리면 커넥션이 유지되지 않는 문제가 발생한다. 
  - 이 커넥션은 이후 로직은 물론이고, 트랜잭션을 종료(커밋, 롤백)할 때 까지 살아있어야 한다.
- DataSourceUtils.releaseConnection() 을 사용하면 커넥션을 바로 닫는 것이 아니다.
  - 트랜잭션을 사용하기 위해 동기화된 커넥션은 커넥션을 닫지 않고 그대로 유지해준다.
  - 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫는다.

## 트랜잭션 템플릿
- 트랜잭션을 사용하는 로직을 살펴보면 다음과 같은 패턴이 반복되는 것을 확인할 수 있다.
  - 트랜잭션 사용 코드
  ```java
  //트랜잭션 시작
  TransactionStatus status = transactionManager.getTransaction(new
  DefaultTransactionDefinition());
  
  try {
   //비즈니스 로직
   bizLogic(fromId, toId, money);
   transactionManager.commit(status); //성공시 커밋
  } catch (Exception e) {
   transactionManager.rollback(status); //실패시 롤백
   throw new IllegalStateException(e);
  }
  ```
  - 트랜잭션을 시작하고, 비즈니스 로직을 실행하고, 성공하면 커밋하고, 예외가 발생해서 실패하면 롤백한다.
  - 다른 서비스에서 트랜잭션을 시작하려면 try , catch , finally 를 포함한 성공시 커밋, 실패시 롤백 코드가 반복될 것이다.
  - 이런 형태는 각각의 서비스에서 반복된다. 달라지는 부분은 비즈니스 로직 뿐이다.
  - 이럴 때 템플릿 콜백 패턴을 활용하면 이런 반복 문제를 깔끔하게 해결할 수 있다.
- 트랜잭션 템플릿
  - 템플릿 콜백 패턴을 적용하려면 템플릿을 제공하는 클래스를 작성해야 하는데, 
    - 스프링은 TransactionTemplate 라는 템플릿 클래스를 제공한다.
  - TransactionTemplate
  ```java
  public class TransactionTemplate {
   private PlatformTransactionManager transactionManager;
   public <T> T execute(TransactionCallback<T> action){..}
   void executeWithoutResult(Consumer<TransactionStatus> action){..}
  }
  ```
  - execute() : 응답 값이 있을 때 사용한다.
  - executeWithoutResult() : 응답 값이 없을 때 사용한다.

- 정리
  - 트랜잭션 템플릿 덕분에, 트랜잭션을 사용할 때 반복하는 코드를 제거할 수 있었다.
  - 하지만 이곳은 서비스 로직인데 비즈니스 로직 뿐만 아니라 트랜잭션을 처리하는 기술 로직이 함께 포함되어 있다.
  - 애플리케이션을 구성하는 로직을 핵심 기능과 부가 기능으로 구분하자면 서비스 입장에서 비즈니스 로직은 핵심 기능이고, 트랜잭션은 부가 기능이다.
  - 이렇게 비즈니스 로직과 트랜잭션을 처리하는 기술 로직이 한 곳에 있으면 두 관심사를 하나의 클래스에서 처리하게 된다. 
    - 결과적으로 코드를 유지보수하기 어려워진다.
  - 서비스 로직은 가급적 핵심 비즈니스 로직만 있어야 한다. 
    - 하지만 트랜잭션 기술을 사용하려면 어쩔 수 없이 트랜잭션 코드가 나와야 한다. 
    - 어떻게 하면 이 문제를 해결할 수 있을까?