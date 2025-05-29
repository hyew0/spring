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

# 요구 사항 추가

- 타임리프를 사용해서 폼에서 체크박스, 라디오 버튼, 셀렉트 박스를 편리하게 사용하는 방법을 학습한다.
- 기존 상품 서비스에 아래의 요구 사항을 추가한다.
  - 판매 여부
    - 판매 오픈 여부
    - 체크 박스로 선택할 수 있다.
  - 등록 지역
    - 서울, 부산, 제주
    - 체크 박스로 다중 선택할 수 있다.
  - 상품 종류
    - 도서, 식품, 기타
    - 라디오 버튼으로 하나만 선택할 수 있다.
  - 배송 방식
    - 빠른 배송
    - 일반 배송
    - 느린 배송
    - 셀렉트 박스로 하나만 선택할 수 있다.

# 체크 박스
- 체크 박스를 체크하면 HTML Form에서 open=on 이라는 값이 넘어간다. 
  - 스프링은 on 이라는 문자를 true 타입으로 변환해준다.
- 주의 - 체크 박스를 선택하지 않을 때
  - HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 open 이라는 필드 자체가 서버로 전송되지 않는다.

- HTTP 요청 메시지 로깅
  - HTTP 요청 메시지를 서버에서 보고 싶으면 다음 설정을 추가하면 된다.
  - application.properties
  ```
  logging.level.org.apache.coyote.http11=trace
  ```

- HTML checkbox는 선택이 안되면 값 자체를 보내지 않기 때문에 문제가 될 수 있다.
  - 스프링 MVC는 히든 필드 _open을 만들어서 기존이름에 언더스코어를 붙여서 전송하면 체크를 해제했다고 인식한다.
    - 체크를 해제한 경우 여기에서 open은 전송되지 않고, _open만 전송되어 -> 스프링 MVC는 체크를 해제했다고 판단한다.
      - <input type="hidden" name="_open" value="on"/>

## 체크 박스 - 멀티
- @ModelAttribute의 특별한 사용법 
  - 등록 폼, 상세화면, 수정 폼에서 모두 서울, 부산, 제주라는 체크 박스를 반복해서 보여주어야 한다. 
  - 이렇게 하려면 각각의 컨트롤러에서 model.addAttribute(...) 을 사용해서 체크 박스를 구성하는 데이터를 반복해서 넣어주어야 한다.
  - @ModelAttribute 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
  - 이렇게하면 해당 컨트롤러를 요청할 때 regions 에서 반환한 값이 자동으로 모델( model )에 담기게 된다.
  - 물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.

- th:for="${#ids.prev('regions')}"
  - 멀티 체크박스는 같은 이름의 여러 체크박스를 만들 수 있다. 
  - 그런데 문제는 이렇게 반복해서 HTML 태그를 생성할 때, 생성된 HTML 태그 속성에서 name 은 같아도 되지만, id 는 모두 달라야 한다. 
  - 따라서 타임리프는 체크박스를 each 루프 안에서 반복해서 만들 때 임의로 1 , 2 , 3 숫자를 뒤에 붙여준다.

# 라디오 버튼
- 라디오 버튼은 여러 선택지 중에 하나를 선택할 때 사용할 수 있다