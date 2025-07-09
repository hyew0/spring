# 1. 데이터 접근 기술 

## 데이터 접근 기술 진행 방식
- 실무에서 주로 사용하는 다음과 같은 다양한 데이터 접근 기술들을 학습한다.

- 적용 데이터 접근 기술
  - JdbcTemplate
  - MyBatis
  - JPA, Hibernate
  - 스프링 데이터 JPA
  - Querydsl
- 이 기술들은 크게 2가지(SQLMapper, ORM 관련 기술)로 분류된다.
  - SQLMapper 관련 기술
    - JdbcTemplate
    - MyBatis
    - SQLMapper 주요 기능
      - 개발자가 SQL을 작성하면 SQL 결과를 객체로 편리하게 매핑해준다.
      - JDBC를 직접 사용할 때 발생하는 여러 중복을 제거하고, 기타 개발자에게 여러가지 편리한 기능을 제공한다.
  - ORM 관련 기술
    - JPA, Hibernate
    - 스프링 데이터 JPA
    - Querydsl
    - ORM 주요 기능
      - JdbcTemplate이나 MyBatis 같은 SQL 매퍼 기술은 SQL을 개발자가 직접 작성해야 하지만, JPA를 사용하면 기본적인 SQL은 JPA가 대신 작성하고 처리해준다
        - 개발자는 저장하고 싶은 객체를 자바 컬렉션에 저장하고 조회하듯이 사용하면 ORM 기술이 데이터베이스에 해당 객체를 저장하고 조회해준다.
      - JPA는 자바 진영의 ORM 표준이고, Hibernate는 JPA에서 가장 많이 사용하는 구현체이다.
        - 자바에서 ORM을 사용할 때는 JPA 인터페이스를 사용하고, 그 구현체로 하이버네이트를 사용한다고 생각하면 된다.
      - 스프링 데이터 JPA, Querydsl은 JPA를 더 편리하게 사용할 수 있게 도와주는 프로젝트이다.
        - 실무에서는 꼭 같이 사용하는 편이 좋다.