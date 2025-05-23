# Servlet

- 서블릿은 톰캣 같은 웹 애플리케이션 서버를 직접 설치하고,그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린 다음, 톰캣 서버를 실행하면 된다. 하지만 이 과정은 매우 번거롭다.
  - 스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.

- @ServletComponentScan
  - 스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 @ServletComponentScan 을 지원한다.
  ```java
    @ServletComponentScan //서블릿 자동 등록
    @SpringBootApplication
  ```
  - 다음과 같이 시작 클래스에 추가해준다.

- @WebServlet(name = "helloServlet", urlPatterns = "/hello")
  - 서블릿 애노테이션
    - name
      - 서블릿 이름
    - urlPatterns
      - URL 매핑
  - HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 매서드를 실행
    - protected void service(HttpServletRequest request, HttpServletResponse response)

### HTTP 요청 메시지 로그로 확인하기
- 다음 설정을 추가. resource -> application.properties
  - application.properties
    ```properties
        logging.level.org.apache.coyote.http11=debug
    ```

### HttpServletRequest
  - HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다. 
    - 서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다. 
    - 그리고 그 결과를 HttpServletRequest 객체에 담아서 제공한다.
  - HTTP 요청 메시지 조회 가능.
    - START LINE
      - HTTP 메소드
      - URL
      - 쿼리 스트링
      - 스키마, 프로토콜
    - 헤더
      - 헤더 조회
    - 바디
      - form 파라미터 형식 조회
      - message body 데이터 직접 조회
  - HttpServletRequest 객체는 추가로 여러가지 부가기능도 함께 제공한다
    - 임시 저장소 기능
      -  해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능
        - 저장: request.setAttribute(name, value)
        - 조회: request.getAttribute(name)
    - 세션 관리 기능
      - request.getSession(create: true)

- HttpServletRequest - 기본 사용법
  - HttpServletRequest가 제공하는 기본 기능들
  - http://localhost:8080/request-header?username=hello -> 예시 URL
    - 1. REQUEST Line
      - request.getMe0thod() 
        - ex) GET
      - request.getProtocol()
        - ex) HTTP/1.1
      - request.getScheme()
        - ex) http
      - request.getRequestURL()
        - http://localhost:8080/request-header
      - request.getRequestURI()
        - /request-header
      - request.getQueryString()
        - username=hello
      - request.isSecure() 
        - false
    - 2. Header 모든 정보
      - request.getHeaderNames().asIterator()
        .forEachRemaining(headerName -> System.out.println(headerName + ": " + request.getHeader(headerName)));
        - host: localhost:8080
          connection: keep-alive
          sec-ch-ua: "Chromium";v="136", "Google Chrome";v="136", "Not.A/Brand";v="99"
          sec-ch-ua-mobile: ?0
          sec-ch-ua-platform: "Windows"
          upgrade-insecure-requests: 1
          user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36
          accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
          sec-fetch-site: none
          sec-fetch-mode: navigate
          sec-fetch-user: ?1
          sec-fetch-dest: document
          accept-encoding: gzip, deflate, br, zstd
          accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
    - 3. Header 편리한 조회
      - Host 편의 조회
         - request.getServerName() 
           - localhost
         - request.getServerPort() 
           - 8080
      - Accept-Language 편의 조회
         - request.getLocales().asIterator()
           .forEachRemaining(locale -> System.out.println("locale = " + locale));
           - locale = ko_KR
             locale = ko
             locale = en_US
             locale = en
         - System.out.println("request.getLocale() = " + request.getLocale());
           - request.getLocale() = ko_KR
      - cookie 편의 조회
         - if (request.getCookies() != null) {
             for (Cookie cookie : request.getCookies()) {
               System.out.println(cookie.getName() + ": " + cookie.getValue());
             }
           }
      - Content 편의 조회
         - request.getContentType() 
           - null 
         - request.getContentLength() 
           - -1 
         - request.getCharacterEncoding() 
           - UTF-8
    - 4. 기타 정보
      - Remote 정보
        - request.getRemoteHost() 
          - 0:0:0:0:0:0:0:1
        - request.getRemoteAddr() 
          - 0:0:0:0:0:0:0:1
        - request.getRemotePort() 
          - 65158
      - Local 정보
        - request.getLocalName() 
          - 0:0:0:0:0:0:0:1 
        - request.getLocalAddr()  
          - 0:0:0:0:0:0:0:1
        - request.getLocalPort() 
          - 8080

### HTTP 요청 데이터
- HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법

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

#### GET - 쿼리 파라미터
- 쿼리 파라미터는 URL에 다음과 같이 ? 를 시작으로 보낼 수 있다. 추가 파라미터는 & 로 구분하면 된다
  - http://localhost:8080/request-param?username=hello&age=20
- 쿼리 파라미터 조회 메서드
  - String username = request.getParameter("username"); //단일 파라미터 조회 
  - Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
  - Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map으로 조회
  - String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회

- 복수 파라미터에서 단일 파라미터 조회
  - username=hello&username=kim 과 같이 파라미터 이름은 하나인데, 값이 중복이면 어떻게 될까?
    - request.getParameter() 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용해야 한다. 
    - 지금처럼 중복일 때는 request.getParameterValues() 를 사용해야 한다.
    - 참고로 이렇게 중복일 때 request.getParameter() 를 사용하면 request.getParameterValues() 의 첫번째 값을 반환한다.

#### POST HTML Form
- 특징
  - content-type: application/x-www-form-urlencoded
  - 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. username=hello&age=20
- application/x-www-form-urlencoded 형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다. 
  - 따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다.
  - 클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만, 서버 입장에서는 둘의 형식이 동일하므로, request.getParameter() 로 편리하게 구분없이 조회할 수 있다.
- request.getParameter() 는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML Form 형식도 둘 다 지원한다.


#### API 메시지 바디 - 단순 텍스트
- HTTP message body에 데이터를 직접 담아서 요청
  - HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용
  - POST, PUT, PATCH
- HTTP 메시지 바디의 데이터를 InputStream을 사용해서 직접 읽을 수 있다.

#### API 메시지 바디 - JSON
-  HTTP API에서 주로 사용하는 JSON 형식으로 데이터를 전달

- JSON 형식 전송
  - POST http://localhost:8080/request-body-json
  - content-type: application/json
  - message body: {"username": "hello", "age": 20}
  - 결과: messageBody = {"username": "hello", "age": 20}

  - 참고
    - JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다. 
    - 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리( ObjectMapper )를 함께 제공한다.
  - 참고
    - HTML form 데이터도 메시지 바디를 통해 전송되므로 직접 읽을 수 있다. 
    - 하지만 편리한 파리미터 조회 기능( request.getParameter(...) )을 이미 제공하기 때문에 파라미터 조회 기능을 사용하면 된다.

### HttpServletResponse

- HttpServletResponse 역할
  - HTTP 응답 메시지 생성
    - HTTP 응답코드 지정
    - 헤더 생성
    - 바디 생성
  - 편의 기능 제공
    - Content-Type, 쿠키, Redirect

#### HTTP 응답 데이터 - 단순 텍스트, HTML

- HTTP 응답 메시지는 주로 다음 내용을 담아서 전달한다.
  - 단순 텍스트 응답
    - 앞에서 살펴봄 ( writer.println("ok"); )
  - HTML 응답
  - HTTP API - MessageBody JSON 응답
- HTTP 응답으로 HTML을 반환할 때는 content-type을 text/html 로 지정해야 한다

#### HTTP 응답 데이터 - API JSON
- HTTP 응답으로 JSON을 반환할 때는 content-type을 application/json 로 지정해야 한다.
- Jackson 라이브러리가 제공하는 objectMapper.writeValueAsString() 를 사용하면 객체를 JSON 문자로 변경할 수 있다.

### 템플릿 엔진
- 지금까지 서블릿과 자바 코드만으로 HTML을 만들어보았다. 
  - 서블릿 덕분에 동적으로 원하는 HTML을 마음껏 만들 수 있다. 
  - 정적인 HTML 문서라면 화면이 계속 달라지는 회원의 저장 결과라던가, 회원 목록 같은 동적인 HTML을 만드는 일은 불가능 할 것이다.
  - 그런데, 코드에서 보듯이 이것은 매우 복잡하고 비효율 적이다. 
  - 자바 코드로 HTML을 만들어 내는 것 보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것이다.
  - 이것이 바로 템플릿 엔진이 나온 이유이다. 
  - 템플릿 엔진을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.
  - 템플릿 엔진에는 JSP, Thymeleaf, Freemarker, Velocity등이 있다.

# jsp로 회원관리 

- JSP는 자바 코드를 그대로 다 사용할 수 있다.
  - <%@ page import="hello.servlet.domain.member.MemberRepository" %>
    - 자바의 import 문과 같다.
  - <% ~~ %>
    - 이 부분에는 자바 코드를 입력할 수 있다.
  - <%= ~~ %>
    - 이 부분에는 자바 코드를 출력할 수 있다.

## 서블릿과 JSP의 한계와 MVC 패턴의 등장

- 서블릿
  - html 코드와 자바 코드가 섞여서 지저분하고 복잡했음.
- jsp 도입
  - JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용
  - jsp 한계
    - 회원 저장 JSP를 보면 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 회원 목록의 경우에도 마찬가지.
    - JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다. JSP가 너무 많은 역할을 한다. 
      - 이렇게 작은 프로젝트도 벌써 머리가 아파오는데, 수백 수천줄이 넘어가는 JSP를 떠올려보면 유지보수 측면에서 나쁜 코드이다.
- MVC 패턴 등장
  - 비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 하기 위해 과거 개발자들도 모두 비슷한 고민이 했었고, 그래서 MVC 패턴이 등장했다.

# MVC 패턴

- Model View Controller
  - MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와 뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다.
  - 웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.

- 컨트롤러: 
  - HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 
  - 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
- 모델: 
  - 뷰에 출력할 데이터를 담아둔다. 
  - 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
- 뷰: 
  - 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 
  - 여기서는 HTML을 생성하는 부분을 말한다.