# 로그

- 로그 라이브러리는 Logback, Log4J, Log4J2 등등 수 많은 라이브러리가 있는데, 그것을 통합해서 인터페이스로 제공하는 것이 바로 SLF4J 라이브러리다.
- 쉽게 이야기해서 SLF4J는 인터페이스이고, 그 구현체로 Logback 같은 로그 라이브러리를 선택하면 된다.
- 실무에서는 스프링 부트가 기본으로 제공하는 Logback을 대부분 사용한다

- 로그 선언 
  - private Logger log = LoggerFactory.getLogger(getClass());
  - private static final Logger log = LoggerFactory.getLogger(Xxx.class)
  - @Slf4j : 롬복 사용 가능
- 로그 호출
  - log.info("hello")
  - System.out.println("hello")

- 매핑 정보
  - @RestController
    - @Controller 는 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
    - @RestController 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다. 
      - 따라서 실행 결과로 ok 메세지를 받을 수 있다. @ResponseBody 와 관련이 있는데, 뒤에서 더 자세히 설명한다.
- 테스트
  - 로그가 출력되는 포멧 확인
    - 시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스명, 로그 메시지
  - 로그 레벨 설정을 변경해서 출력 결과를 보자.
    - LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
    - 개발 서버는 debug 출력
    - 운영 서버는 info 출력
  - @Slf4j 로 변경

- 올바른 로그 사용법
  - log.debug("data="+data)
    - 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 "data="+data가 실제 실행이 되어 버린다. 
    - 결과적으로 문자 더하기 연산이 발생한다.
  - log.debug("data={}", data)
    - 로그 출력 레벨을 info로 설정하면 아무일도 발생하지 않는다. 
    - 따라서 앞과 같은 의미없는 연산이 발생하지 않는다.

- 로그 사용시 장점
  - 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
  - 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있다.
  - 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 
    - 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다.
  - 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등) 그래서 실무에서는 꼭 로그를 사용해야 한다.

# HTTP 요청 - 기본, 헤더 조회
- HttpServletRequest
- HttpServletResponse
- HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
- Locale : Locale 정보를 조회한다.
- @RequestHeader MultiValueMap<String, String> headerMap
  - 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
- @RequestHeader("host") String host
  - 특정 HTTP 헤더를 조회한다.
  - 속성
    - 필수 값 여부: required
    - 기본 값 속성: defaultValue
- @CookieValue(value = "myCookie", required = false) String cookie
  - 특정 쿠키를 조회한다.
  - 속성
    - 필수 값 여부: required
    - 기본 값: defaultValue

- MultiValueMap
  - MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
  - HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
    - keyA=value1&keyA=value2

- @Slf4j
  - 다음 코드를 자동으로 생성해서 로그를 선언해준다. 개발자는 편리하게 log 라고 사용하면 된다. 
  ```
  private static final org.slf4j.Logger log =
  org.slf4j.LoggerFactory.getLogger(RequestHeaderController.class);
  ```
# HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

- 클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다.
  - GET - 쿼리 파라미터
    - /url**?username=hello&age=20**
    - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
    - 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
  - POST - HTML Form
    - content-type: application/x-www-form-urlencoded
    - 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
    - 예) 회원 가입, 상품 주문, HTML Form 사용
  - HTTP message body에 데이터를 직접 담아서 요청
    - HTTP API에서 주로 사용, JSON, XML, TEXT
    - 데이터 형식은 주로 JSON 사용
    - POST, PUT, PATCH

# HTTP 요청 파라미터 - @RequestParam

- @RequestParam : 파라미터 이름으로 바인딩
- @ResponseBody : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력

- @RequestParam의 name(value) 속성이 파라미터 이름으로 사용
 -  @RequestParam("username") String memberName
   - request.getParameter("username"

# HTTP 요청 파라미터 - @ModelAttribute
- 롬복 @Data
  - @Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를 자동으로 적용해준다.

- 스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
  - HelloData 객체를 생성한다.
  - 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.
  - 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.

- 프로퍼티
  - 객체에 getUsername() , setUsername() 메서드가 있으면, 이 객체는 username 이라는 프로퍼티를 가지고 있다.
  - username 프로퍼티의 값을 변경하면 setUsername() 이 호출되고, 조회하면 getUsername() 이 호출된다. 
  ```
    class HelloData {
    getUsername();
    setUsername();
    }
  ```

- 바인딩 오류
  - age=abc 처럼 숫자가 들어가야 할 곳에 문자를 넣으면 BindException 이 발생한다. 
  - 이런 바인딩 오류를 처리하는 방법은 검증 부분에서 다룬다.

- @ModelAttribute 는 생략할 수 있다.
  - 그런데 @RequestParam 도 생략할 수 있으니 혼란이 발생할 수 있다.
  - 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
    - String , int , Integer 같은 단순 타입 = @RequestParam
    - 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)

# HTTP 요청 메시지 - 단순 텍스트

- HTTP message body에 데이터를 직접 담아서 요청
  - HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용
  - POST, PUT, PATCH

- 요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 @RequestParam , @ModelAttribute 를 사용할 수 없다. (물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정된다.)

- 스프링 MVC는 다음 파라미터를 지원한다.
  - InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
  - OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

- 스프링 MVC는 다음 파라미터를 지원한다.
  - HttpEntity: HTTP header, body 정보를 편리하게 조회
    - 메시지 바디 정보를 직접 조회
    - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
  - HttpEntity는 응답에도 사용 가능
    - 메시지 바디 정보 직접 반환
    - 헤더 정보 포함 가능
    - view 조회X

- 참고
  - 스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지 컨버터( HttpMessageConverter )라는 기능을 사용한다. 
  - 이것은 조금 뒤에 HTTP 메시지 컨버터에서 자세히 설명한다.

- @RequestBody
  - @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 
  - 참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
  - 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와는 전혀 관계가 없다.

- 요청 파라미터 vs HTTP 메시지 바디
  - 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
  - HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody

- @ResponseBody
  - @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
  - 물론 이 경우에도 view를 사용하지 않는다.

# HTTP 요청 메시지 - JSON

# HTTP 응답 - 정적 리소스, 뷰 템플릿

- 스프링(서버)에서 응답 데이터를 만드는 방법은 크게 3가지이다.
  - 정적 리소스
    - 예) 웹 브라우저에 정적인 HTML, css, js를 제공할 때는, 정적 리소스를 사용한다.
  - 뷰 템플릿 사용
    - 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용한다.
  - HTTP 메시지 사용
    - HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.

### 정적 리소스
- 스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다.
  - /static , /public , /resources , /META-INF/resources
- src/main/resources 는 리소스를 보관하는 곳이고, 또 클래스패스의 시작 경로이다.
  - 따라서 다음 디렉토리에 리소스를 넣어두면 스프링 부트가 정적 리소스로 서비스를 제공한다.
- 정적 리소스 경로
  - src/main/resources/static
  - 다음 경로에 파일이 들어있으면
    - src/main/resources/static/basic/hello-form.html
  - 웹 브라우저에서 다음과 같이 실행하면 된다.
    - http://localhost:8080/basic/hello-form.html
    - 정적 리소스는 해당 파일을 변경 없이 그대로 서비스하는 것이다.

### 뷰 템플릿
- 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다.
  - 일반적으로 HTML을 동적으로 생성하는 용도로 사용하지만, 다른 것들도 가능하다. 
  - 뷰 템플릿이 만들 수 있는 것이라면 뭐든지 가능하다.
- 스프링 부트는 기본 뷰 템플릿 경로를 제공한다.
- 뷰 템플릿 경로
  - src/main/resources/templates

- Thymeleaf 스프링 부트 설정
  - 다음 라이브러리를 추가하면(이미 추가되어 있다.)
  - build.gradle
  ```
  `implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`
  ```
  - 스프링 부트가 자동으로 ThymeleafViewResolver 와 필요한 스프링 빈들을 등록한다. 그리고 다음 설정도 사용한다. 이 설정은 기본 값 이기 때문에 변경이 필요할 때만 설정하면 된다.
  - application.properties
  ```
  spring.thymeleaf.prefix=classpath:/templates/
  spring.thymeleaf.suffix=.html
  ```
  
# HTTP 응답 - HTTP API, 메시지 바디에 직접 입력
- HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.
