# 타임리프 알아보기

- 타임리프 사용 선언
  ```html
    <html xmlns:th="http://www.thymeleaf.org">
    ```
- 속성 변경- th:href
  - th:href="@{/css/bootstrap.min.css}"
    - href="value1" 을 th:href="value2" 의 값으로 변경한다.
    - 타임리프 뷰 템플릿을 거치게 되면 원래 값을 th:xxx 값으로 변경한다. 만약 값이 없다면 새로 생성한다.
    - HTML을 그대로 볼 때는 href 속성이 사용되고, 뷰 템플릿을 거치면 th:href 의 값이 href 로 대체되면서 동적으로 변경할 수 있다.
    - 대부분의 HTML 속성을 th:xxx 로 변경할 수 있다.

- 타임리프 핵심
  - 핵심은 th:xxx 가 붙은 부분은 서버사이드에서 렌더링 되고, 기존 것을 대체한다. 
    - th:xxx 이 없으면 기존 html 의 xxx 속성이 그대로 사용된다.
  - HTML을 파일로 직접 열었을 때, th:xxx 가 있어도 웹 브라우저는 th: 속성을 알지 못하므로 무시한다.
  - 따라서 HTML을 파일 보기를 유지하면서 템플릿 기능도 할 수 있다.

- URL 링크 표현식 - @{...}
  - th:href="@{/css/bootstrap.min.css}"
    - @{...} : 타임리프는 URL 링크를 사용하는 경우 @{...} 를 사용한다. 이것을 URL 링크 표현식이라 한다.
    - URL 링크 표현식을 사용하면 서블릿 컨텍스트를 자동으로 포함한다.

- 속성 변경 - th:onclick
  - onclick="location.href='addForm.html'"
  - th:onclick="|location.href='@{/basic/items/add}'|"

- 리터럴 대체 - |...|
  - |...| :이렇게 사용한다.
    - 타임리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야 한다.
      - <span th:text="'Welcome to our application, ' + ${user.name} + '!'">
    - 다음과 같이 리터럴 대체 문법을 사용하면, 더하기 없이 편리하게 사용할 수 있다.
        - <span th:text="|Welcome to our application, ${user.name}!|">
    - 결과를 다음과 같이 만들어야 하는데
      - location.href='/basic/items/add'
    - 그냥 사용하면 문자와 표현식을 각각 따로 더해서 사용해야 하므로 다음과 같이 복잡해진다.
      - th:onclick="'location.href=' + '\'' + @{/basic/items/add} + '\''"
    - 리터럴 대체 문법을 사용하면 다음과 같이 편리하게 사용할 수 있다.
      - th:onclick="|location.href='@{/basic/items/add}'|"

- 반복 출력 - th:each
  - <tr th:each="item : ${items}">
  - 반복은 th:each 를 사용한다. 
    - 이렇게 하면 모델에 포함된 items 컬렉션 데이터가 item 변수에 하나씩 포함 되고, 반복문 안에서 item 변수를 사용할 수 있다.
  - 컬렉션의 수 만큼 <tr>..</tr> 이 하위 테그를 포함해서 생성된다.

- 변수 표현식 - ${...}
  - <td th:text="${item.price}">10000</td>
  - 모델에 포함된 값이나, 타임리프 변수로 선언한 값을 조회할 수 있다.
  - 프로퍼티 접근법을 사용한다. ( item.getPrice() )

- 내용 변경 - th:text
  - <td th:text="${item.price}">10000</td>
  - 내용의 값을 th:text 의 값으로 변경한다.
  - 여기서는 10000을 ${item.price} 의 값으로 변경한다.

- URL 링크 표현식2 - @{...},
  - th:href="@{/basic/items/{itemId}(itemId=${item.id})}"
  - 상품 ID를 선택하는 링크를 확인해보자.
  - URL 링크 표현식을 사용하면 경로를 템플릿처럼 편리하게 사용할 수 있다.
  - 경로 변수( {itemId} ) 뿐만 아니라 쿼리 파라미터도 생성한다.
  - 예) th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}"
    - 생성 링크: http://localhost:8080/basic/items/1?query=test

- URL 링크 간단히
  - th:href="@{|/basic/items/${item.id}|}"
  - 상품 이름을 선택하는 링크를 확인해보자.
  - 리터럴 대체 문법을 활용해서 간단히 사용할 수도 있다.

- 속성 변경 - th:value
  - th:value="${item.id}"
    - 모델에 있는 item 정보를 획득하고 프로퍼티 접근법으로 출력한다. ( item.getId() )
    - value 속성을 th:value 속성으로 변경한다.

## 상품 등록 

- 속성 변경 - th:action
  - th:action
  - HTML form에서 action 에 값이 없으면 현재 URL에 데이터를 전송한다.
  - 상품 등록 폼의 URL과 실제 상품 등록을 처리하는 URL을 똑같이 맞추고 HTTP 메서드로 두 기능을 구분한다.
    - 상품 등록 폼: GET /basic/items/add
    - 상품 등록 처리: POST /basic/items/add
  - 이렇게 하면 하나의 URL로 등록 폼과, 등록 처리를 깔끔하게 처리할 수 있다.
  
  - 취소
    - 취소시 상품 목록으로 이동한다.
    - th:onclick="|location.href='@{/basic/items}'|

## 상품 등록 처리 - @ModelAttribute

- 상품 등록 폼은 다음 방식으로 서버에 데이터를 전달한다.
  - POST - HTML Form
    - content-type: application/x-www-form-urlencoded
    - 메시지 바디에 쿼리 파리미터 형식으로 전달 itemName=itemA&price=10000&quantity=10
    - 예) 회원 가입, 상품 주문, HTML Form 사용

- @ModelAttribute - 요청 파라미터 처리
  - @ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
- @ModelAttribute - Model 추가
  - @ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다. 
  - 지금 코드를 보면 model.addAttribute("item", item) 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다

- 주의
  - @ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 등록한다.
    - 예) @ModelAttribute 클래스명 모델에 자동 추가되는 이름
      - Item -> item
      - HelloWorld -> helloWorld

- 리다이렉트
  - 상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
  - 스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원한다.
  - redirect:/basic/items/{itemId}
    - 컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
    - redirect:/basic/items/{itemId} {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용한다.

- 참고
  - HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
  - PUT, PATCH는 HTTP API 전송시에 사용
  - 스프링에서 HTTP POST로 Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청이다.

# PRG Post/Redirect/Get 

- 지금까지 진행한 상품 등록 처리 컨트롤러는 심각한 문제가 있다. (addItemV1 ~ addItemV4)
  - 상품 등록을 완료하고 웹 브라우저의 새로고침 버튼을 클릭해보면 상품이 계속해서 중복 등록되는 것을 확인할 수 있다.

- 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
  - 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다.
  - 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
  - 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.

- 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
  - 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
  - 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다. 
  - 따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
  - 이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.

- 주의
  - "redirect:/basic/items/" + item.getId() redirect에서 +item.getId() 처럼 URL에 변수를 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다. 
  - 다음에 설명하는 RedirectAttributes 를 사용하자.

# RedirectAttributes

- RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVariable , 쿼리 파라미터까지 처리해준다.
  - redirect:/basic/items/{itemId}
  - pathVariable 바인딩: {itemId}
  - 나머지는 쿼리 파라미터로 처리: ?status=true

- resources/templates/basic/item.html
```
<h2 th:if="${param.status}" th:text="'저장 완료!'"></h2> 
``` 
- th:if : 해당 조건이 참이면 실행
  - ${param.status} : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능
  - 원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다. 
    - 그런데 쿼리 파라미터는 자주 사용해서 타임리프에서 직접 지원한다.