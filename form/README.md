# 타임리프 스프링 통합

- 타임리프는 크게 2가지 메뉴얼을 제공한다.
  - 기본 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
  - 스프링 통합 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

- 타임리프는 스프링 없이도 동작하지만, 스프링과 통합을 위한 다양한 기능을 편리하게 제공한다.

- 스프링 통합으로 추가되는 기능들
  - 스프링의 SpringEL 문법 통합
  - ${@myBean.doSomething()} 처럼 스프링 빈 호출 지원
  - 편리한 폼 관리를 위한 추가 속성
    - th:object (기능 강화, 폼 커맨드 객체 선택)
    - th:field , th:errors , th:errorclass
  - 폼 컴포넌트 기능
    - checkbox, radio button, List 등을 편리하게 사용할 수 있는 기능 지원
  - 스프링의 메시지, 국제화 기능의 편리한 통합
  - 스프링의 검증, 오류 처리 통합
  - 스프링의 변환 서비스 통합(ConversionService)

- 타임리프 설정 방법
  - 타임리프 템플릿 엔진을 스프링 빈에 등록하고, 타임리프용 뷰 리졸버를 스프링 빈으로 등록하는 방법
    - https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#the-springstandard-dialect
    - https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#views-and-view-resolvers

- 스프링 부트는 build.gradle에 아래 코드를 넣어주면 타임리프와 관련된 라이브러리를 다운받고, 스프링 부트는 타임리프와 관련된 설정용 스프링 빈을 자동으로 등록해준다.
  - implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
- 타임리프 관련 설정을 변경하고 싶으면 아래 사이트를 참고해서 application.properties에 추가하면 된다.
  - 스프링 부트가 제공하는 타임리프 설정, thymeleaf 검색 필요
    - https://docs.spring.io/spring-boot/appendix/application-properties/index.html\#appendix.application-properties.templating

