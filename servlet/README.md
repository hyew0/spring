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

- HttpServletRequest
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