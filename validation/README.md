#검증

## BindingResult
- 스프링이 제공하는 검증 오류 처리 방법에서의 핵심은 BindingResult이다.

- 주의
  - BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.

- 필드 오류 - FieldError
  ```java
      if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
      }
  ```
  - 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아두면 된다.
        ```java
            public FieldError(String objectName, String field, String defaultMessage) {}
        ```
    - 필드 파라미터 정리
      - objectName : @ModelAttribute 이름
      - field : 오류가 발생한 필드 이름
      - defaultMessage : 오류 기본 메시지
- 글로벌 오류 - ObjectError
    ```java
        bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
    ``` 
  - 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult 에 담아두면 된다.
    ```java
        public ObjectError(String objectName, String defaultMessage) {}
    ```
  - 글로벌 오류 파라미터 정리
    - objectName : @ModelAttribute 의 이름
    - defaultMessage : 오류 기본 메시지

### 타임리프 스프링 검증 오류 통합 기능
- 타임리프는 스프링의 BindingResult 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.
  - #fields : #fields 로 BindingResult 가 제공하는 검증 오류에 접근할 수 있다.
  - th:errors : 해당 필드에 오류가 있는 경우에 태그를 출력한다. th:if 의 편의 버전이다.
  - th:errorclass : th:field 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.
  
  - 검증과 오류 메시지 공식 메뉴얼
    - https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-errormessages

## BindingResult2
- 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다.
- 예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
  - BindingResult 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
  - BindingResult 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를 정상 호출한다.
- BindingResult에 검증 오류를 적용하는 3가지 방법
  - @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
  - 개발자가 직접 넣어준다.
  - Validator 사용 -> 이것은 뒤에서 설명.
- 주의!
  - BindingResult 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 
    - 예를 들어서 @ModelAttribute Item item , 바로 다음에 BindingResult 가 와야 한다.
  - BindingResult 는 Model에 자동으로 포함된다.

- BindingResult와 Errors
  - org.springframework.validation.Errors
  - org.springframework.validation.BindingResult

  - BindingResult 는 인터페이스이고, Errors 인터페이스를 상속받고 있다.
    - 실제 넘어오는 구현체는 BeanPropertyBindingResult 라는 것인데, 둘다 구현하고 있으므로 BindingResult 대신에 Errors 를 사용해도 된다. Errors 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다.
    - BindingResult 는 여기에 더해서 추가적인 기능들을 제공한다. 
    - addError() 도 BindingResult 가 제공하므로 여기서는 BindingResult 를 사용하자. 
      - 주로 관례상 BindingResult 를 많이 사용한다.

- -> 이렇게 BindingResult , FieldError , ObjectError 를 사용해서 오류 메시지를 처리하는 방법을 사용하면 되지만 한가지 문제점이 있음.
  - 오류가 발생하면 고객이 입력한 내용이 모두 사라지는 문제 발생. 아래의 방법으로 해결가능.

## FieldError, ObjectError
- 목표
  - 사용자 입력 오류 메시지가 화면에 남도록 하자.
    - 예) 가격을 1000원 미만으로 설정시 입력한 값이 남아있어야 한다.
  - FieldError , ObjectError 에 대해서 더 자세히 알아보자.

- FieldError 생성자
  - FieldError 는 두 가지 생성자를 제공한다.
    ```java
        public FieldError(String objectName, String field, String defaultMessage);
        public FieldError(String objectName, String field, @Nullable Object
        rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable
        Object[] arguments, @Nullable String defaultMessage)
    ```
  - 파라미터 목록
    - objectName : 오류가 발생한 객체 이름
    - field : 오류 필드
    - rejectedValue : 사용자가 입력한 값(거절된 값), 오류 발생시 사용자 입력 값을 저장하는 필드
    - bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 , 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다
    - codes : 메시지 코드
    - arguments : 메시지에서 사용하는 인자
    - defaultMessage : 기본 오류 메시지
  - 오류 발생시 사용자 입력 값 유지
    ```java
        new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~1,000,000 까지 허용합니다.")
    ```
    
  - 타임리프의 사용자 입력 값 유지
    - th:field="*{price}"
    - 타임리프의 th:field 는 매우 똑똑하게 동작하는데, 정상 상황에는 모델 객체의 값을 사용하지만, 
      - 오류가 발생하면 FieldError 에서 보관한 값을 사용해서 값을 출력한다
  - 스프링의 바인딩 오류 처리
    - 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다. 
    - 그리고 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출한다. 
    - 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.