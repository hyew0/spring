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

## 프로젝트 구조
-  build.gradle
- spring-boot-starter-thymeleaf : 타임리프 사용
- spring-boot-starter-web : 스프링 웹, MVC 기능 사용
- spring-boot-starter-test : 스프링이 제공하는 테스트 기능
- lombok : lombok을 추가로 테스트에서도 사용하는 설정 주의
    - ```
      //테스트에서 lombok 사용
      testCompileOnly 'org.projectlombok:lombok'
      testAnnotationProcessor 'org.projectlombok:lombok'
   ```
- repository
    - interface
    - 실제 구현체들(메모리 저장소)
    - 주로 findAll은 검색 조건을 받아 내부에서 데이터를 검색하는 기능을 한다.
- Dto
    - data transfer object
    - 데이터 전송 객체
    - DTO는 기능은 없고 데이터를 전달만 하는 용도로 사용되는 객체를 뜻한다.
        - DTO에 반드시 기능이 있으면 안되는가하면 그건 아니다.
            - 객체의 주목적이 데이터를 전송하는 것을 모두 DTO라고 생각하면 된다.
    - 또한 객체 이름에 DTO를 반드시 붙여야 하는 것은 아니지만 붙여두면 용도를 명확히 알 수 있다.
    - 규칙은 정해진 것이 없기 때문에 프로젝트 내ㅔ서 일관성 있게 규칙을 정하면 된다.
- service
    - 서비스 인터페이스
        - 사실 서비스는 구현체를 변경할 일이 많지 않아서 인터페이스를 잘 사용하지 않는다.
    - 서비스 구현체
    - 서비스는 비즈니스 로직을 처리하는 곳이다.
    - 서비스는 주로 컨트롤러에서 호출되어 비즈니스 로직을 처리하고, 결과를 반환한다.
- controller
    - 컨트롤러는 주로 HTTP 요청을 처리하는 곳이다.
    - 컨트롤러는 서비스와 협력하여 HTTP 요청을 처리하고, 결과를 반환한다.
    - ```java
    @Controller
    @RequiredArgsConstructor
    public class HomeController {
      @RequestMapping("/")
      public String home() {
        return "redirect:/items";
      }
    }
    ```
### 설정
- config
    - 예시
        - ```java
      package hello.itemservice.config;
      import hello.itemservice.repository.ItemRepository;
      import hello.itemservice.repository.memory.MemoryItemRepository;
      import hello.itemservice.service.ItemService;
      import hello.itemservice.service.ItemServiceV1;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      
      @Configuration
      public class MemoryConfig {
        
        @Bean
        public ItemService itemService() {
          return new ItemServiceV1(itemRepository());
        }
      
        @Bean
        public ItemRepository itemRepository() {
          return new MemoryItemRepository();
        }
      }
      ```
    - ItemServiceV1 , MemoryItemRepository 를 스프링 빈으로 등록하고 생성자를 통해 의존관계를 주입한다.
    - 참고로 여기서는 서비스와 리포지토리는 구현체를 편리하게 변경하기 위해, 이렇게 수동으로 빈을 등록했다.
    - 컨트롤러는 컴포넌트 스캔을 사용한다.
- testDataInit
    - 테스트용 데이터를 넣어주는 클래스
        - 애플리케이션 실행 시 초기 데이터를 저장하는 역할.
        - 리스트에서 데이터가 잘 나오는지 편리하게 확인할 용도로 사용한다.
            - 이 기능이 없으면 서버를 실행할 때 마다 데이터를 입력해야 리스트에 나타난다. (메모리여서 서버를 내리면 데이터가 제거된다.)
    - @EventListener(ApplicationReadyEvent.class) :
        - 스프링 컨테이너가 완전히 초기화를 다 끝내고, 실행 준비가 되었을 때 발생하는 이벤트이다.
        - 스프링이 이 시점에 해당 애노테이션이 붙은 메서드를 호출해준다.
            - 참고로 이 기능 대신 @PostConstruct 를 사용할 경우
                - AOP 같은 부분이 아직 다 처리되지 않은 시점에 호출될 수 있기 때문에, 간혹 문제가 발생할 수 있다.
                - 예를 들어서 @Transactional 과 관련된 AOP가 적용되지 않은 상태로 호출될 수 있다.
            - @EventListener(ApplicationReadyEvent.class) 는 AOP를 포함한 스프링 컨테이너가 완전히 초기화 된 이후에 호출되기 때문에 이런 문제가 발생하지 않는다.

#### 프로필
- 스프링은 로딩 시점에 application.properties 의 spring.profiles.active 속성을 읽어서 프로필로 사용한다.
- 이 프로필은 로컬(나의 PC), 운영 환경, 테스트 실행 등등 다양한 환경에 따라서 다른 설정을 할 때 사용하는 정보이다.
- 예를 들어서 로컬PC에서는 로컬 PC에 설치된 데이터베이스에 접근해야 하고, 운영 환경에서는 운영 데이터베이스에 접근해야 한다면 서로 설정 정보가 달라야 한다.
    - 심지어 환경에 따라서 다른 스프링 빈을 등록해야 할 수 도 있다.
    - 프로필을 사용하면 이런 문제를 깔끔하게 해결할 수 있다.
- main 프로필
    - /src/main/resources 하위의 application.properties
    - ```properties
    spring.profiles.active=local 
    ```
        - 이 위치의 application.properties 는 /src/main 하위의 자바 객체를 실행할 때 (주로 main() ) 동작하는 스프링 설정이다.
        - spring.profiles.active=local 이라고 하면 스프링은 local 이라는 프로필로 동작한다.
        - 따라서 직전에 설명한 @Profile("local") 가 동작하고, testDataInit 가 스프링 빈으로 등록 된다.
- test 프로필
    - /src/test/resources 하위의 application.properties
    - ```properties
    spring.profiles.active=test 
    ```
    - 이 위치의 application.properties 는 /src/test 하위의 자바 객체를 실행할 때 동작하는 스프링 설정이다.
    - 주로 테스트 케이스를 실행할 때 동작한다.
    - spring.profiles.active=test 로 설정하면 스프링은 test 라는 프로필로 동작한다.
        - 이 경우 직전에 설명한 @Profile("local") 는 프로필 정보가 맞지 않아서 동작하지 않는다.
        - 따라서 testDataInit 이라는 스프링 빈도 등록되지 않고, 초기 데이터도 추가하지 않는다.
### 테스트
- afterEach() :
    - 테스트는 서로 영향을 주면 안된다. 따라서 각각의 테스트가 끝나고 나면 저장한 데이터를 제거해야 한다.
    - @AfterEach 는 각각의 테스트의 실행이 끝나는 시점에 호출된다.
    - itemservice-db, 여기서는 메모리 저장소를 완전히 삭제해서 다음 테스트에 영향을 주지 않도록 초기화 한다.
    - 인터페이스에는 clearStore() 가 없기 때문에 MemoryItemRepository 인 경우에만 다운 케스팅을 해서 데이터를 초기화한다.
    - 실제 DB를 사용하는 경우에는 테스트가 끝난 후에 트랜잭션을 롤백해서 데이터를 초기화 할 수 있다.

## 데이터베이스 테이블 생성
- 다양한 데이터 접근 기술들을 활용해서 메모리가 아닌 데이터베이스에 데이터를 보관하는 방법을 알아보자.
- 먼저 H2 데이터베이스에 접근해서 item 테이블을 생성하자.
- ```sql
  drop table if exists item CASCADE;
  create table item
  (
    id bigint generated by default as identity,
    item_name varchar(10),
    price integer,
    quantity integer,
    primary key (id)
  );
  ```
    - generated by default as identity
        - identity 전략이라고 하는데, 기본 키 생성을 데이터베이스에 위임하는 방법이다.
        - MySQL의 Auto Increment와 같은 방법이다.
        - 여기서 PK로 사용되는 id 는 개발자가 직접 지정하는 것이 아니라 비워두고 저장하면 된다.
        - 그러면 데이터 베이스가 순서대로 증가하는 값을 사용해서 넣어준다.
- 테이블을 생성했으면, 잘 동작하는지 다음 SQL을 실행하고 조회해보자.
- 등록 쿼리
    - ```sql
    insert into item(item_name, price, quantity) values ('ItemTest', 10000, 10) 
    ```
- 조회 쿼리
    - ```sql
    select * from item; 
    ```
- 실행하면 데이터베이스가 생성한 id 값을 포함해서 등록한 데이터가 잘 저장되어 있는 것을 확인할 수 있다.

#### 참고 - 권장하는 식별자 선택 전략
- 데이터베이스 기본 키는 다음 3가지 조건을 모두 만족해야 한다.
    - 1.null 값은 허용하지 않는다.
    - 2.유일해야 한다.
    - 3.변해선 안 된다.
- 테이블의 기본 키를 선택하는 전략은 크게 2가지가 있다.
    - 자연 키(natural key)
        - 비즈니스에 의미가 있는 키
        - 예: 주민등록번호, 이메일, 전화번호
    - 대리 키(surrogate key)
        - 비즈니스와 관련 없는 임의로 만들어진 키, 대체 키로도 불린다.
        - 예: 오라클 시퀀스, auto_increment, identity, 키생성 테이블 사용
- 자연 키보다는 대리 키를 권장한다
    - 자연 키와 대리 키는 일장 일단이 있지만 될 수 있으면 대리 키의 사용을 권장한다.
    - 예를 들어 자연 키인 전화번호를 기본 키로 선택한다면 그 번호가 유일할 수는 있지만, 전화번호가 없을 수도 있고 전화번호가 변경될 수도 있다.
    - 따라서 기본 키로 적당하지 않다.
    - 문제는 주민등록번호처럼 그럴듯하게 보이는 값이다. 이 값은 null 이 아니고 유일하며 변하지 않는다는 3가지 조건을 모두 만족하는 것 같다.
    - 하지만 현실과 비즈니스 규칙은 생각보다 쉽게 변한다.
    - 주민등록번호 조차도 여러 가지 이유로 변경될 수 있다.
- 비즈니스 환경은 언젠가 변한다
    - 기본 키의 조건을 현재는 물론이고 미래까지 충족하는 자연 키를 찾기는 쉽지 않다.
    - 대리 키는 비즈니스와 무관한 임의의 값이므로 요구사항이 변경되어도 기본 키가 변경되는 일은 드물다.
    - 대리 키를 기본 키로 사용하되 주민등록번호나 이메일처럼 자연 키의 후보가 되는 컬럼들은 필요에 따라 유니크 인덱스를 설정해서 사용하는 것을 권장한다.
    - 참고로 JPA는 모든 엔티티에 일관된 방식으로 대리 키 사용을 권장한다
    - 비즈니스 요구사항은 계속해서 변하는데 테이블은 한 번 정의하면 변경하기 어렵다.
    - 그런면에서 외부 풍파에 쉽게 흔들리지 않는 대리 키가 일반적으로 좋은 선택이라 생각한다. 

# 2. 스프링 JdbcTemplate
## JdbcTemplate 기본
- SQL을 직접 사용하는 경우에 스프링이 제공하는 JdbcTemplate 은 아주 좋은 선택지이다.
  - JdbcTemplate 은 JDBC를 편리하게 사용할 수 있게 도와주는 스프링의 기술이다.
  - 장점
    - 설정의 편리함
      - JdbcTemplate 은 spring-jdbc 라이브러리에 포함되어 있는데, 이 라이브러리는 스프링으로 JDBC를 사용할 때 기본으로 사용되는 라이브러리이다.
      - 그리고 별도의 복잡한 설정 없이 바로 사용 가능하다.
    - 반복 문제 해결
      - JdbcTemplate은 템플릿 콜백 패턴을 사용해서, JDBC를 직접 사용할 때 발생하는 대부분의 반복 작업을 대신 처리해준다.
      - 개발자는 SQL을 작성하고, 전달할 파리미터를 정의하고, 응답 값을 매핑하기만 하면 된다.
      - 우리가 생각할 수 있는 대부분의 반복 작업을 대신 처리해준다.
        - 커넥션 획득
        - statement 를 준비하고 실행
        - 결과를 반복하도록 루프를 실행
        - 커넥션 종료, statement , resultset 종료
        - 트랜잭션 다루기 위한 커넥션 동기화
        - 예외 발생시 스프링 예외 변환기 실행
  - 단점
    - 동적 SQL을 해결하기 어렵다.
### JdbcTemplate 설정
- build.gradle
    ```groovy
     dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        
        //JdbcTemplate 추가
        implementation 'org.springframework.boot:spring-boot-starter-jdbc'
        //H2 데이터베이스 추가
        runtimeOnly 'com.h2database:h2'
     
        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
     
        //테스트에서 lombok 사용
        testCompileOnly 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'
     }
  ```
  - org.springframework.boot:spring-boot-starter-jdbc 를 추가하면 JdbcTemplate이 들어있는 spring-jdbc 가 라이브러리에 포함된다.
    - 여기서는 H2 데이터베이스에 접속해야 하기 때문에 H2 데이터베이스의 클라이언트 라이브러리(Jdbc Driver)도 추가하자.
    - runtimeOnly 'com.h2database:h2'
  - JdbcTemplate은 spring-jdbc 라이브러리만 추가하면 된다. 
    - 별도의 추가 설정 과정은 없다.

## 동적 쿼리 문제 
- 검색을 하려고 할 때, 검색하는 값에 따라서 실행하는 SQL이 동적으로 달라져야 한다.
    - 예
      - 검색 조건 x
      - 상품명으로 검색
      - 최대가격으로 검색
      - 상품명, 최대 가격으로 둘 다 검색
    - 다음의 경우와 같이 상황에 따라 SQL을 동적으로 생성해야 한다.
      - 그리고 각 상황에 맞추어 파라미터도 생성해야 한다.
      - 이후에 사용할 MyBatis의 가장 큰 장점이 SQL을 직접 사용할 때 동적 쿼리를 쉽게 작성할 수 있다는 것임.
- 로그 추가
  - JdbcTemplate이 실행하는 SQL 로그를 확인하려면 application.properties에 아래의 설정을 추가하면 된다.
  - main, test 설정이 분리되어 있기 때문에 둘 다 확인하려면 두 곳 모두에 추가해야 한다.
  - ```properties
    #jdbcTemplate sql log
    logging.level.org.springframework.jdbc=debug
    ```