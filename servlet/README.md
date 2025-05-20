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
      1. Host 편의 조회
         - request.getServerName() 
           - localhost
         - request.getServerPort() 
           - 8080
      2. Accept-Language 편의 조회
         - request.getLocales().asIterator()
           .forEachRemaining(locale -> System.out.println("locale = " + locale));
           - locale = ko_KR
             locale = ko
             locale = en_US
             locale = en
         - System.out.println("request.getLocale() = " + request.getLocale());
           - request.getLocale() = ko_KR
      3. cookie 편의 조회
         - if (request.getCookies() != null) {
             for (Cookie cookie : request.getCookies()) {
               System.out.println(cookie.getName() + ": " + cookie.getValue());
             }
           }
      4. Content 편의 조회
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