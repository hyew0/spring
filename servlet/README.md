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
- 다음 설정을 추가하자.
  - application.properties
    ```properties
        logging.level.org.apache.coyote.http11=debug
    ```
