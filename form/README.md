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

# 입력 폼 처리

- 타임리프가 제공하는 입력 폼 기능을 적용해서 개선할 수 있다.
  - th:object : 커맨드 객체를 지정한다.
  - *{...} : 선택 변수 식이라고 한다. th:object 에서 선택한 객체에 접근한다.
  - th:field
    - HTML 태그의 id , name , value 속성을 자동으로 처리해준다.

- 렌더링 전
    ```html
    <input type="text" th:field="*{itemName}" />
    ```
- 렌더링 후
    ```html
    <input type="text" id="itemName" name="itemName" th:value="*{itemName}" />
    ```
- th:object="${item}" : <form> 에서 사용할 객체를 지정한다. 선택 변수 식( *{...} )을 적용할 수 있다.
- th:field="*{itemName}"
  - *{itemName} 는 선택 변수 식을 사용했는데, ${item.itemName} 과 같다. 앞서 th:object 로 item 을 선택했기 때문에 선택 변수 식을 적용할 수 있다.
  - th:field 는 id , name , value 속성을 모두 자동으로 만들어준다.
    - id : th:field 에서 지정한 변수 이름과 같다. id="itemName"
    - name : th:field 에서 지정한 변수 이름과 같다. name="itemName"
    - value : th:field 에서 지정한 변수의 값을 사용한다. value=""
