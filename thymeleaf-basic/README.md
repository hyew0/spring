# 타임리프 - 기본 기능

- https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html

### 타임리프 특징

- 서버 사이드 HTML 렌더링 (SSR)
- 네츄럴 템플릿
- 스프링 통합 지원

- 서버 사이드 렌더링
  - 타임 리프는 백엔드 서버에서 HTML을 동적으로 렌더링하는 용도로 사용된다.

- 네츄럴 템플릿
  - 타임리프는 순수 HTML을 최대한 유지하는 특징이 있다.
  - 타임리프로 작성된 파일은 해당 파일을 그대로 웹 브라우저에서 열어도 정상적인 HTML결과를 확인 할 수 있다.
  - 순수 HTML을 그래로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 "네츄럴 템플릿(natural templates)"라 한다.

- 스프링 통합 지원
  - 타임리프는 스프링과 자연스럽게 통합되고, 스프링의 다양한 기능을 편리하게 사용할 수 있게 지원한다.

### 타임리프 기본 기능

- 타임리프 사용 선언
```html
<html xmlns:th="http://www.thymeleaf.org">
```

- 기본 표현식
- 타임리프는 아래의 기본 표현식들을 제공한다.
```html
- 간단한 표현:
◦ 변수 표현식: ${...}
◦ 선택 변수 표현식: *{...}
◦ 메시지 표현식: #{...}
◦ 링크 URL 표현식: @{...}
◦ 조각 표현식: ~{...}

- 리터럴
◦ 텍스트: 'one text', 'Another one!',…
◦ 숫자: 0, 34, 3.0, 12.3,…
◦ 불린: true, false
◦ 널: null
◦ 리터럴 토큰: one, sometext, main,…

- 문자 연산:
◦ 문자 합치기: +
◦ 리터럴 대체: |The name is ${name}|

- 산술 연산:
◦ Binary operators: +, -, *, /, %
◦ Minus sign (unary operator): -

- 불린 연산:
◦ Binary operators: and, or
◦ Boolean negation (unary operator): !, not

- 비교와 동등:
◦ 비교: >, <, >=, <= (gt, lt, ge, le)
◦ 동등 연산: ==, != (eq, ne)

- 조건 연산:
◦ If-then: (if) ? (then)
◦ If-then-else: (if) ? (then) : (else)
◦ Default: (value) ?: (defaultvalue)

- 특별한 토큰:
◦ No-Operation: _
```
- 참고
  - https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax

#### 텍스트 - text, utext
- 텍스트를 출력하는 기능, HTML의 콘텐츠에 데이터를 출력하려고 할 때는 th:text 사용
    ```
    <span th:text="${data}">
    ```
- HTML 태그의 속성이 아니라 HTML 콘텐츠 영역 안에서 직접 데이터를 출력하고 싶으면 [[...]] 를 사용하면 된다.
  ```
  컨텐츠 안에서 직접 출력하기 = [[${data}]]
  ```
  
- Escape
  - HTML 문서는 < , > 같은 특수 문자를 기반으로 정의된다. 
  - 따라서 뷰 템플릿으로 HTML 화면을 생성할 때는 출력하는 데이터에 이러한 특수 문자가 있는 것을 주의해서 사용해야 한다.
    - 변경 전
      - "Hello Spring!"
    - 변경 후
      - "Hello <b>Spring!</b>"
  - HTML 엔티티
    - 웹 브라우저는 < 를 HTML 테그의 시작으로 인식한다. 
    - 따라서 < 를 태그의 시작이 아니라 문자로 표현할 수 있는 방법이 필요한데, 이것을 HTML 엔티티라 한다. 
    - 그리고 이렇게 HTML에서 사용하는 특수 문자를 HTML 엔티티로 변경하는 것을 이스케이프(escape)라 한다. 
    - 그리고 타임리프가 제공하는 th:text , [[...]] 는 기본적으로 이스케이프(escape)를 제공한다.
      - "<" -> &lt;
      - ">" -> &gt; 
      - 기타 수 많은 HTML 엔티티가 있다.

- Unescape
  - 이스케이프 기능을 사용하지 않기 위해 타임리프가 사용하는 방버
    - 타임리프는 다음 두 기능을 제공한다.
      - th:text -> th:utext
      - [[...]] -> [(...)]
  

#### 변수 - SpringEL
- 타임리프에서 변수를 사용할 때는 변수 표현식을 사용한다.
- 변수 표현식 : ${...}
  - 이 변수 표현식에는 스프링 EL이라는 스프링이 제공하는 표현식을 사용할 수 있다.

- SpringEL 다양한 표현식 사용
  - object
    - user.username : user의 username을 프로퍼티 접근 -> user.getUsername()
    - user['username'] : 위와 같음 -> user.getUsername()
    - user.getUsername() : user의 getUsername() 을 직접 호출
  - list
    - users[0].username : List에서 첫 번째 회원을 찾고 username 프로퍼티 접근 -> list.get(0).getUsername()
    - users[0]['username'] : 위와 같음
    - users[0].getUsername() : List에서 첫 번째 회원을 찾고 메서드 직접 호출
  - map
    - userMap['userA'].username : Map에서 userA를 찾고, username 프로퍼티 접근 -> map.get("userA").getUsername()
    - userMap['userA']['username'] : 위와 같음
    - userMap['userA'].getUsername() : Map에서 userA를 찾고 메서드 직접 호출
  
- 지역 변수 선언
  - th: with 을 사용하면 지역 변수를 선언해서 사용할 수 있다.
  - 지역 변수는 선언한 태그 안에서만 사용할 수 있다.


--- 
