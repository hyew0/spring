# 정적 컨텐츠
## 스프링 부트 정적 켄텐츠 기능
https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-application

# MVC
- MVC : Model, View, Controller

# API
- @ResponseBody를 사용하면 뷰 리졸버(viewResolver)를 사용하지 않는다.
- 대신 http의 body에 문자 내용을 직접 반환한다
  - HTML BODY TAG를 말하는 것이 아님.
- @ResponseBody를 사용하고, 객체를 반환하면 객체가 JSON으로 반환된다.

## @ResponseBody 사용 원리
- @ResponseBody 를 사용
  - HTTP의 BODY에 문자 내용을 직접 반환
  - viewResolver 대신에 HttpMessageConverter 가 동작
  - 기본 문자처리: StringHttpMessageConverter
  - 기본 객체처리: MappingJackson2HttpMessageConverter
  - byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음

# 웹 애플리케이션 계층 구조
- 컨트롤러: 웹 MVC의 컨트롤러 역할을 함.
- 서비스: 핵심 비즈니스 로직 구현
- 리포지토리: 데이터베이스에 접근,도메인 객체를 DB에 저장하고 관리
- 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨

# 테스트

- @AfterEach
  - 여러 테스트를 동시에 실행 시 메모리 DB에 직전 테스트 결과가 남을 수 있다. 
  - 이러면 다음 테스트가 이전 테스트 결과로 인해 실패가 될 수 있다. 
  - @AfterEach를 사용하여 각 테스트가 종료될 때마다 해당 어노테이션을 수행한다.
  - 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니므로 테스트는 각각 독립적으로 실행되어야 한다.
  ```java
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    ```
- @BeforeEach 
  - 각 테스트 실행 전에 호출된다. 
  - 테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존관계도 새로 맺어준다.