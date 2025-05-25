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

## mvc 패턴 적용 예제

- 서블릿을 컨트롤러로 사용하고, JSP를 뷰로 사용해서 MVC 패턴을 적용해보자.
  - Model은 HttpServletRequest 객체를 사용한다. request는 내부에 데이터 저장소를 가지고 있는데,
  - request.setAttribute() , request.getAttribute() 를 사용하면 데이터를 보관하고, 조회할 수 있다.

- dispatcher.forward() : 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이 발생한다

- /WEB-INF
  - 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다. 
  - 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.

- redirect vs forward
  - 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청한다.
    - 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다. 
  - 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

- HttpServletRequest를 Model로 사용한다.
  - request가 제공하는 setAttribute() 를 사용하면 request 객체에 데이터를 보관해서 뷰에 전달할 수 있다.
  - 뷰는 request.getAttribute() 를 사용해서 데이터를 꺼내면 된다.

- <%= request.getAttribute("member")%> 로 모델에 저장한 member 객체를 꺼낼 수 있지만, 너무 복잡해진다.
  - JSP는 ${} 문법을 제공하는데, 이 문법을 사용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다.
  ```html
    <ul>
      <li>id=${member.id}</li>
      <li>username=${member.username}</li>
      <li>age=${member.age}</li>
    </ul>
  ```
  
- 모델에 담아둔 members를 JSP가 제공하는 taglib기능을 사용해서 반복하면서 출력했다.
  - members 리스트에서 member 를 순서대로 꺼내서 item 변수에 담고, 출력하는 과정을 반복한다.
  - <c:forEach> 이 기능을 사용하려면 다음과 같이 선언해야 한다.
    - <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
## MVC 패턴 - 한계

- 공통 처리가 어렵다는 문제가 있다.
  - ->  이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다. 
  - 소위 수문장 역할을 하는 기능이 필요하다.
    - 프론트 컨트롤러(Front Controller) 패턴을 도입하면 이런 문제를 깔끔하게 해결할 수 있다.(입구를 하나로!)
    - 스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.

# FrontController

- FrontController 패턴 특징
  - 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
  - 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
  - 입구를 하나로 하여 공통 처리 간ㅇ
  - 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨.

- 스프링 웹 MVC와 프론트 컨트롤러
  - 스프링 웹 MVC 의 핵심도 FronteController임
  - 스프링 웹 MVC 의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음

## 성능의 개선

- 현재 v3와 v4는 방식이 다른 인터페이스이다. 
  - 프론트 컨트롤러가 한가지 방식의 컨트롤러 인터페이스만을 사용할 수 있을 때 여러 개의 인터페이스를 적용하고 싶다면 "어댑터"를 사용하면 된다.

- 핸들러 어댑터: 
  - 중간에 어댑터 역할을 하는 어댑터가 추가되었는데 이름이 핸들러 어댑터이다. 
  - 여기서 어댑터 역할을 해주는 덕분에 다양한 종류의 컨트롤러를 호출할 수 있다.
- 핸들러(컨트롤러): 
  - 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경했다. 
  - 그 이유는 이제 어댑터가 있기 때문에 꼭 컨트롤러의 개념 뿐만 아니라 어떠한 것이든 해당하는 종류의 어댑터만 있으면 다 처리할 수 있기 때문이다.


---
- 정리
  - v1: 프론트 컨트롤러를 도입
    - 기존 구조를 최대한 유지하면서 프론트 컨트롤러를 도입
  - v2: View 분류
    - 단순 반복 되는 뷰 로직 분리
  - v3: Model 추가
    - 서블릿 종속성 제거
    - 뷰 이름 중복 제거
  - v4: 단순하고 실용적인 컨트롤러
    - v3와 거의 비슷
    - 구현 입장에서 ModelView를 직접 생성해서 반환하지 않도록 편리한 인터페이스 제공
  - v5: 유연한 컨트롤러
    - 어댑터 도입
    - 어댑터를 추가해서 프레임워크를 유연하고 확장성 있게 설계

- -> 지금까지 작성한 코드는 스프링 MVC 프레임워크의 핵심 코드의 축약 버전이고, 구조도 거의 같다.

---
# 스프링 MVC 기능 이해

- 직접 만든 프레임워크 ->  스프링 MVC 비교
  - FrontController -> DispatcherServlet
  - handlerMappingMap -> HandlerMapping
  - MyHandlerAdapter -> HandlerAdapter
  - ModelView -> ModelAndView
  - viewResolver -> ViewResolver
  - MyView -> View 

- 스프링 MVC도 프론트 컨트롤러 패턴으로 구현되어 있음.
  - 스프링 MVC의 프론트 컨트롤러가 DispatcherServlet이다.
  - DispatcherServlet 서블릿 등록
    - DispatcherServlet 도 부모 클래스에서 HttpServlet 을 상속 받아서 사용하고, 서블릿으로 동작한다.
      - DispatcherServlet FrameworkServlet HttpServletBean HttpServlet
    - 스프링 부트는 DispatcherServlet 을 서블릿으로 자동으로 등록하면서 모든 경로( urlPatterns="/" )에 대해서 매핑한다.
      - 참고: 더 자세한 경로가 우선순위가 높다. 그래서 기존에 등록한 서블릿도 함께 동작한다.

- 동작 순서
  - 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
  - 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
  - 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
  - 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
  - ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다.
  - viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
    - JSP의 경우: InternalResourceViewResolver 가 자동 등록되고, 사용된다.
  - View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
    - JSP의 경우 InternalResourceView(JstlView) 를 반환하는데, 내부에 forward() 로직이 있다.
  - 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다

- 주요 인터페이스 목록
  - 핸들러 매핑: org.springframework.web.servlet.HandlerMapping
  - 핸들러 어댑터: org.springframework.web.servlet.HandlerAdapter
  - 뷰 리졸버: org.springframework.web.servlet.ViewResolver
  - 뷰: org.springframework.web.servlet.View

- 스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터 (실제로는 더 많지만, 중요한 부분 위주로 설명하기 위해 일부 생략)
  - HandlerMapping
  ```
  0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다. ```
  ```

  - HandlerAdapter
  ```
  0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
  2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리
  ```

- viewResolver
  - 뷰 리졸버 - InternalResourceViewResolver
  - 스프링 부트는 InternalResourceViewResolver 라는 뷰 리졸버를 자동으로 등록하는데, 이때 application.properties 에 등록한 spring.mvc.view.prefix , spring.mvc.view.suffix 설정 정보를 사용해서 등록한다.
    ```properties
    spring.mvc.view.prefix=/WEB-INF/views/
    spring.mvc.view.suffix=.jsp 
    ```
    
  - 스프링 부트가 자동 등록하는 뷰 리졸버(실제로는 더 많지만, 중요한 부분 위주로 설명하기 위해 일부 생략)  
  ```
    1 = BeanNameViewResolver : 빈 이름으로 뷰를 찾아서 반환한다. (예: 엑셀 파일 생성 기능에 사용)
    2 = InternalResourceViewResolver : JSP를 처리할 수 있는 뷰를 반환한다
  ```
- 순서
  -  핸들러 어댑터 호출
     - 핸들러 어댑터를 통해 new-form 이라는 논리 뷰 이름을 획득한다.
  - ViewResolver 호출
    - new-form 이라는 뷰 이름으로 viewResolver를 순서대로 호출한다. 
    - BeanNameViewResolver 는 new-form 이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다. 
    - InternalResourceViewResolver 가 호출된다.
  - InternalResourceViewResolver
     - 이 뷰 리졸버는 InternalResourceView 를 반환한다. 
  - 뷰 - InternalResourceView
    - InternalResourceView 는 JSP처럼 포워드 forward() 를 호출해서 처리할 수 있는 경우에 사용한다.
  - view.render()
    - view.render() 가 호출되고 InternalResourceView 는 forward() 를 사용해서 JSP를 실행한다

  - 참고
    - InternalResourceViewResolver 는 만약 JSTL 라이브러리가 있으면 InternalResourceView 를 상속받은 JstlView 를 반환한다. 
      - JstlView 는 JSTL 태그 사용시 약간의 부가 기능이 추가된다.
    - 다른 뷰는 실제 뷰를 렌더링하지만, JSP의 경우 forward() 통해서 해당 JSP로 이동(실행)해야 렌더링이 된다. 
      - JSP를 제외한 나머지 뷰 템플릿들은 forward() 과정 없이 바로 렌더링 된다.
    - Thymeleaf 뷰 템플릿을 사용하면 ThymeleafViewResolver 를 등록해야 한다. 
      - 최근에는 라이브러리만 추가하면 스프링 부트가 이런 작업도 모두 자동화해준다.

## 스프링 MVC 시작하기

- @RequestMapping
  - 스프링은 애노테이션을 활용한 매우 유연하고 실용적인 컨트롤러를 만들었다.
    - 그게 바로 @RequestMapping 이다.
  - @RequestMapping
    - RequestMappingHandlerMapping
    - RequestMappingHandlerAdapter
    - -> 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터이다. 
    - 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 핸들러 매핑과 어댑터이다. 실무에서도 이것을 거의 대부분 사용한다. (99.9%)

- @Controller
  - 스프링이 자동으로 스프링 빈으로 등록한다.
    - 내부에 @Component 애노테이션이 있어서 컴포넌트 스캔의 대상이 됨
- @RequestMapping
  - 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다.
  - 애노테이션을 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
- ModelAndView
  - 모델과 뷰 정보를 담아서 반환하면 된다.

- RequestMappingHandlerMapping 은 스프링 빈 중에서 @RequestMapping 또는 @Controller 가 클래스 레벨에 붙어 있는 경우에 매핑 정보로 인식한다.

- 주의!! - 스프링 3.0 이상
  - 스프링 부트 3.0(스프링 프레임워크 6.0)부터는 클래스 레벨에 @RequestMapping 이 있어도 스프링 컨트롤러로 인식하지 않는다. 
  - 오직 @Controller 가 있어야 스프링 컨트롤러로 인식한다. 
  - 참고로 @RestController 는 해당 애노테이션 내부에 @Controller 를 포함하고 있으므로 인식 된다. 
    - 따라서 @Controller 가 없는 위의 두 코드는 스프링 컨트롤러로 인식되지 않는다. 
      - ( RequestMappingHandlerMapping 에서 @RequestMapping 는 이제 인식하지 않고, Controller 만 인식한다.)

# 스프링 MVC - 컨트롤러 통합 (springMVC v2)

- @RequestMapping 을 잘 보면 클래스 단위가 아니라 메서드 단위에 적용된 것을 확인할 수 있다. 따라서 컨트롤러 클래스를 유연하게 하나로 통합할 수 있다.

- 클래스 레벨 @RequestMapping("/springmvc/v2/members")
  - 메서드 레벨 @RequestMapping("/new-form") -> /springmvc/v2/members/new-form
  - 메서드 레벨 @RequestMapping("/save") -> /springmvc/v2/members/save
  - 메서드 레벨 @RequestMapping ->  /springmvc/v2/members

# 스프링 MVC - 실용적인 방식 (springMVC v3)

- Model 파라미터
  - save() , members() 를 보면 Model을 파라미터로 받는 것을 확인할 수 있다. 스프링 MVC도 이런 편의 기능을 제공한다.
- ViewName 직접 반환
  - 뷰의 논리 이름을 반환할 수 있다.
- @RequestParam 사용
  - 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있다.
  - @RequestParam("username") 은 request.getParameter("username") 와 거의 같은 코드라 생각하면 된다.
  - 물론 GET 쿼리 파라미터, POST Form 방식을 모두 지원한다.
- @RequestMapping ->  @GetMapping, @PostMapping
  - @RequestMapping 은 URL만 매칭하는 것이 아니라, HTTP Method도 함께 구분할 수 있다.
  - 예를 들어서 URL이 /new-form 이고, HTTP Method가 GET인 경우를 모두 만족하는 매핑을 하려면 다음과 같이 처리하면 된다.
  ```
    @RequestMapping(value = "/new-form", method = RequestMethod.GET) 
  ```
  - 이것을 @GetMapping , @PostMapping 으로 더 편리하게 사용할 수 있다.
  - 참고로 Get, Post, Put, Delete, Patch 모두 애노테이션이 준비되어 있다.