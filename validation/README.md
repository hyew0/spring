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
    - 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다.                                     c c  
    - 그리고 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출한다. 
    - 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.

## 오류 코드와 메시지 처리

- FieldError , ObjectError 는 다루기 너무 번거롭다.
  - 오류 코드도 좀 더 자동화 할 수 있지 않을까? 예) item.itemName 처럼?
    - 컨트롤러에서 BindResult는 검증해야 할 객체인 target 바로 다음에 온다. 
      - 따라서 BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있다.

- log.info("objectName={}", bindingResult.getObjectName());
- log.info("target={}", bindingResult.getTarget());
  - 컨트롤러에서 다음의 코드를 실행하면 
    - objectName=item //@ModelAttribute name
    - target=Item(id=null, itemName=상품, price=100, quantity=1234)

- rejectValue() , reject()
  - BindingResult 가 제공하는 rejectValue() , reject() 를 사용하면 FieldError , ObjectError 를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다.

  - rejectValue()
  ```java
  void rejectValue(@Nullable String field, String errorCode,
  @Nullable Object[] errorArgs, @Nullable String defaultMessage);
  ```
    - 파라미터
      - field : 오류 필드명
      - errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)
      - errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
      - defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

  - reject()
  ```java
  void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
  ```

- bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null) ```
  - 앞에서 BindingResult 는 어떤 객체를 대상으로 검증하는지 target을 이미 알고 있다고 했다. 
  - 따라서 target(item)에 대한 정보는 없어도 된다. 
  - 오류 필드명은 동일하게 price 를 사용했다.
  
- 축약된 오류 코드
  - FieldError() 를 직접 다룰 때는 오류 코드를 range.item.price 와 같이 모두 입력했다. 
  - 그런데 rejectValue() 를 사용하고 부터는 오류 코드를 range 로 간단하게 입력했다. 
  - 그래도 오류 메시지를 잘 찾아서 출력한다. 무언가 규칙이 있는 것 처럼 보인다. 
    - 이 부분을 이해하려면 MessageCodesResolver 를 이해해야 한다. 뒤에 정리핟록 하겠다.

### 오류 코드  자세히/단순히 작성 가능
- 자세히
  - required.item.itemName : 상품 이름은 필수 입니다.
  - range.item.price : 상품의 가격 범위 오류 입니다.
- 단순히
  - required : 필수 값 입니다.
  - range : 범위 오류 입니다.

- 각각의 단점
  - 단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다. 
  - 반대로 너무 자세하게 만들면 범용성이 떨어진다. 
    - 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.

- 단순/세밀 코드 구분 위해서 우선순위 사용.
  - #Level1
    required.item.itemName: 상품 이름은 필수 입니다.
  - #Level2
    required: 필수 값 입니다.
- 스프링은 MessageCodesResolver 라는 것으로 이러한 기능을 지원한다.

### MessageCodesResolver
- MessageCodesResolver
  - 검증 오류 코드로 메시지 코드들을 생성한다.
  - MessageCodesResolver는 인터페이스이고 DefaultMessageCodesResolver 는 기본 구현체이다.
  - 주로 다음과 함께 사용 ObjectError , FieldError

- DefaultMessageCodesResolver의 기본 메시지 생성 규칙
  - 객체 오류
    - 객체 오류의 경우 다음 순서로 2가지 생성
    - ```java
      1.: code + "." + object name
      2.: code

      예) 오류 코드: required, object name: item
      1.: required.item
      2.: required
      ```
  - 필드 오류
    - 필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성
    - ```java
      1.: code + "." + object name + "." + field
      2.: code + "." + field
      3.: code + "." + field type
      4.: code
      
      예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
      1. "typeMismatch.user.age"
      2. "typeMismatch.age"
      3. "typeMismatch.int"
      4. "typeMismatch"
      ```
- 동작 방식
  - rejectValue() , reject() 는 내부에서 MessageCodesResolver 를 사용한다. 여기에서 메시지 코드들 을 생성한다.
  - FieldError , ObjectError 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
    - MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관한다.
  - 이 부분을 BindingResult 의 로그를 통해서 확인해보자.
    - codes [range.item.price, range.price, range.java.lang.Integer, range]
- FieldError rejectValue("itemName", "required")
  - 다음 4가지 오류 코드를 자동으로 생성
    - required.item.itemName
    - required.itemName
    - required.java.lang.String
    - required
- ObjectError reject("totalPriceMin")
  - 다음 2가지 오류 코드를 자동으로 생성
    - totalPriceMin.item
    - totalPriceMin
- 오류 메시지 출력
  - 타임리프 화면을 렌더링 할 때 th:errors 가 실행된다. 
    - 만약 이때 오류가 있다면 생성된 오류 메시지 코드를 순서대로 돌아가면서 메시지를 찾는다. 
    - 그리고 없으면 디폴트 메시지를 출력한다.

### 오류 코드 관리 전략
- 핵심은 구체적인 것에서 덜 구체적인 것으로.
  - MessageCodesResolver 는 required.item.itemName 처럼 구체적인 것을 먼저 만들어주고, required 처럼 덜 구체적인 것을 가장 나중에 만든다.
    - 이렇게 하면 앞서 말한 것 처럼 메시지와 관련된 공통 전략을 편리하게 도입할 수 있다.
    - 왜 이렇게 사용할까?
      - 모든 오류 코드에 대해서 메시지를 각각 다 정의하면 개발자 입장에서 관리하기 너무 힘들다.
      - 크게 중요하지 않은 메시지는 범용성 있는 requried 같은 메시지로 끝내고, 정말 중요한 메시지는 꼭 필요할 때 구체적으로 적어서 사용하는 방식이 더 효과적이다.
  - 레벨이 높아질수록 = 덜 구체적
    - 구체적인 것에서 덜 구체적인 순서대로 찾는다. 
    - 메시지에 1번이 없으면 2번을 찾고, 2번이 없으면 3번을 찾는다.
      - 이렇게 되면 만약에 크게 중요하지 않은 오류 메시지는 기존에 정의된 것을 그냥 재활용 하면 된다.

#### ValidationUtils
- ValidationUtils 사용 전 
  - ```java
      if (!StringUtils.hasText(item.getItemName())) {
          bindingResult.rejectValue("itemName", "required", "기본: 상품 이름은 필수입니다.");
      }
    ```
- ValidationUtils 사용 후
  - 다음과 같이 한줄로 가능, 제공하는 기능은 Empty , 공백 같은 단순한 기능만 제공
    - ```java
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName","required");
      ```
      
### 스프링이 직접 만든 오류 메시지 처리
- 검증 코드 종류
  - 개발자가 직접 설정한 오류 코드 -> rejectValue() 를 직접 호출
    스프링이 직접 검증 오류에 추가한 경우(주로 타입 정보가 맞지 않음)

- 맞지 않는 타입의 경우
  - price 필드에 문자 "A"를 입력해보자.
    - 로그를 확인해보면 BindingResult 에 FieldError 가 담겨있고, 다음과 같은 메시지 코드들이 생성된 것을 확인할 수 있다.
      - codes[typeMismatch.item.price,typeMismatch.price,typeMismatch.java.lang.Integer,typeMismatch]
      - 다음과 같이 4가지 메시지 코드가 입력되어 있다.
        - typeMismatch.item.price
        - typeMismatch.price
        - typeMismatch.java.lang.Integer
      - typeMismatch
      -그렇다. 스프링은 타입 오류가 발생하면 typeMismatch 라는 오류 코드를 사용한다. 
        - 이 오류 코드가 MessageCodesResolver 를 통하면서 4가지 메시지 코드가 생성된 것이다.
  - errors.properties 에 메시지 코드가 없기 때문에 스프링이 생성한 기본 메시지가 출력된다.
    - errors.properties에 추가하면
      - ```properties
        #추가
        typeMismatch.java.lang.Integer=숫자를 입력해주세요.
        typeMismatch=타입 오류입니다.
        ```
      -  원하는 메시지를 출력하도록 할 수 있다.

## Validator 분리
- 목표
  - 복잡한 검증 로직을 별도로 분리.
- 컨트롤러에서 검증 로직이 차지하는 부분은 매우 크다. 
  - 이런 경우 별도의 클래스로 역할을 분리하는 것이 좋다. 
  - 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다.

- 스프링에서 제공하는 검증 인터페이스
  - supports() {} : 해당 검증기를 지원하는 여부 확인(뒤에서 설명)
  - validate(Object target, Errors errors) : 검증 대상 객체와 BindingResult

- 스프링이 Validator 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서다. 
  - 그런데 앞에서는 검증기를 직접 불러서 사용했고, 이렇게 사용해도 된다. 
    - 그런데 Validator 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.

- WebDataBinder를 통해서 사용하기
  - WebDataBinder 는 스프링의 파라미터 바인딩의 역할을 해주고 검증 기능도 내부에 포함한다.
  - ```java
    @InitBinder
    public void init(WebDataBinder dataBinder) {
      log.info("init binder {}", dataBinder);
      dataBinder.addValidators(itemValidator);
    }
    ```
    - 이렇게 WebDataBinder 에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.
      - @InitBinder 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다. (뒤에 설명)

- @Validated
  - @Validated 는 검증기를 실행하라는 애노테이션이다.
  -  애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행한다. 
    - 그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 
    - 이때 supports() 가 사용된다. 
      - 여기서는 supports(Item.class) 호출되고, 결과가 true 이므로 ItemValidator 의 validate() 가 호출된다.
      - ```java
          @Component
          public class ItemValidator implements Validator {
            @Override
            public boolean supports(Class<?> clazz) {
              return Item.class.isAssignableFrom(clazz);
          }
        
          @Override
            public void validate(Object target, Errors errors) {...}
          }
        ```
- 글로벌 설정 - 모든 컨트롤러에 다 적용
  - ```java
      @SpringBootApplication
      public class ItemServiceApplication implements WebMvcConfigurer {
        public static void main(String[] args) {
          SpringApplication.run(ItemServiceApplication.class, args);
        }
    
        @Override
        public Validator getValidator() {
          return new ItemValidator();
        }
      }
     ```
- 참고
  - 검증시 @Validated @Valid 둘다 사용가능하다.
  - javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다.
    - implementation 'org.springframework.boot:spring-boot-starter-validation'
  - @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다.
  - 자세한 내용은 다음 Bean Validation에서 설명하겠다.

# Bean Validation

## 소개
- 검증을 위에 처럼 매번 코드로 작성하는 것은 번거롭다.
  - 특히 특정 필드에 대한 검증 로직은 대부분 빈 값인지 아닌지, 특정 크기를 넘는지 아닌지와 같이 매우 일반적인 로직.
  - ```java
    public class Item {
      private Long id;
    
      @NotBlank
      private String itemName;
    
      @NotNull
      @Range(min = 1000, max = 1000000)
      private Integer price;
    
      @NotNull
      @Max(9999)
      private Integer quantity;
    
      //...
    }
    ```
- Bean Validation 이란?
  - Bean Validation은 특정한 구현체가 아니라 Bean Validation 2.0(JSR-380)이라는 기술 표준이다. 
  - 쉽게 이야기해서 검증 애노테이션과 여러 인터페이스의 모음이다. 
    - 마치 JPA가 표준 기술이고 그 구현체로 하이버네이트가 있는 것과 같다.

## Bean Validation - 시작

- 스프링과 통합하지 않은 순수한 Bean Validation
  - Bean Validation을 사용하려면 다음 의존관계를 추가해야 한다.
  - build.gradle
    - ```
      implementation 'org.springframework.boot:spring-boot-starter-validation'
      ```
  - Jakarta Bean Validation
    - jakarta.validation-api : Bean Validation 인터페이스
    - hibernate-validator 구현체
- 검증 애노테이션
  - @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다.
  - @NotNull : null 을 허용하지 않는다.
  - @Range(min = 1000, max = 1000000) : 범위 안의 값이어야 한다.
  - @Max(9999) : 최대 9999까지만 허용한다.

- 참고
  - javax.validation 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
  - org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이다. 
  - 실무에서 대부분 하이버네이트 validator를 사용하므로 자유롭게 사용해도 된다.

- 스프링 MVC는 어떻게 Bean Validator 사용?
  - 스프링 부트가 spring-boot-starter-validation 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.
- 스프링 부트는 자동으로 글로벌 Validator로 등록한다.
  - LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다.
  - 이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행한다. 
  - 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid , @Validated 만 적용하면 된다.
  - 검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.
- 주의!
  - 다음과 같이 직접 글로벌 Validator를 직접 등록하면 스프링 부트는 Bean Validator를 글로벌 Validator 로 등록하지 않는다. 
  - 따라서 애노테이션 기반의 빈 검증기가 동작하지 않는다. 다음 부분은 제거하자. 
  - ```java
    @SpringBootApplication
    public class ItemServiceApplication implements WebMvcConfigurer {
    
      // 글로벌 검증기 추가
      @Override
      public Validator getValidator() {
        return new ItemValidator();
      }
    
      // ...
    }
    ```
  - 참고
    - 검증시 @Validated @Valid 둘다 사용가능하다.
      - javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다. (이전에 추가했다.)
      - implementation 'org.springframework.boot:spring-boot-starter-validation'
      - @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다. 
      - 둘중 아무거나 사용해도 동일하게 작동하지만, @Validated 는 내부에 groups 라는 기능을 포함하고 있다. 

- 검증 순서
  - 순서
    - @ModelAttribute 각각의 필드에 타입 변환 시도 
      - 성공하면 다음으로 
      - 실패하면 typeMismatch 로 FieldError 추가 
      - Validator 적용
  - 바인딩에 성공한 필드만 Bean Validation 적용
    - BeanValidator는 바인딩에 실패한 필드는 BeanValidation을 적용하지 않는다.
    - 생각해보면 타입 변환에 성공해서 바인딩에 성공한 필드여야 BeanValidation 적용이 의미 있다.(일단 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증도 의미가 있다.)
  - @ModelAttribute -> 각각의 필드 타입 변환시도 -> 변환에 성공한 필드만 BeanValidation 적용

## Bean Validation - 에러 코드
- Bean Validation이 기본으로 제공하는 오류 메시지를 좀 더 자세히 변경하고 싶으면 어떻게 하면 될까?
- Bean Validation을 적용하고 bindingResult 에 등록된 검증 오류 코드를 보자.
  - 오류 코드가 애노테이션 이름으로 등록된다. 마치 typeMismatch 와 유사하다.
- NotBlank 라는 오류 코드를 기반으로 MessageCodesResolver 를 통해 다양한 메시지 코드가 순서대로 생성된다.
  - @NotBlank
    - NotBlank.item.itemName
    - NotBlank.itemName
    - NotBlank.java.lang.String
    - NotBlank
  - @Range
    - Range.item.price
    - Range.price
    - Range.java.lang.Integer
    - Range

## Bean Validation - 오브젝트 오류
- Bean Validation에서 특정 필드( FieldError )가 아닌 해당 오브젝트 관련 오류( ObjectError )는 어떻게 처리할 수 있을까?
  - 다음과 같이 @ScriptAssert() 를 사용하면 된다.
  - ```java
      @Data
      @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >=10000")
      public class Item {
         //...
      }      
    ```
    
    - 실제 사용해보면 제약이 많고 복잡하다. 그리고 실무에서는 검증 기능이 해당 객체의 범위를 넘어서는 경우들도 종종 등장하는데, 그런 경우 대응이 어렵다.
      - 따라서 오브젝트 오류(글로벌 오류)의 경우 @ScriptAssert 을 억지로 사용하는 것 보다는 다음과 같이 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장한다.
      - ```java
        @PostMapping("/add")
        public String addItem(@Validated @ModelAttribute Item item, BindingResult
        bindingResult, RedirectAttributes redirectAttributes) {
            //특정 필드 예외가 아닌 전체 예외
            if (item.getPrice() != null && item.getQuantity() != null) {
                int resultPrice = item.getPrice() * item.getQuantity();
                if (resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
                }
            }
            if (bindingResult.hasErrors()) {
                log.info("errors={}", bindingResult);
                return "validation/v3/addForm";
            }
        
            //성공 로직
            Item savedItem = itemRepository.save(item);
            redirectAttributes.addAttribute("itemId", savedItem.getId());
            redirectAttributes.addAttribute("status", true);
            return "redirect:/validation/v3/items/{itemId}";
        }
        ```
## Bean Validation - 한계
- 만약 등록과 수정에서의 요구 사항이 다를 시에 이를 적용하고자 하면 기능이 제대로 작동하지 않을 수 있다.
  - 검증 조건의 충돌이 발생하고, 등록과 수정은 같은 BeanValidation을 적용할 수 없다. 이 문제를 해결하려면 어떻게 해야 할까?

## Bean Validation - groups
- 방법 2가지
  - BeanValidation의 groups 기능을 사용한다.
  - Item을 직접 사용하지 않고, ItemSaveForm, ItemUpdateForm 같은 폼 전송을 위한 별도의 모델 객체를 만들어서 사용한다.

- BeanValidation groups 기능 사용
  - 이런 문제를 해결하기 위해 Bean Validation은 groups라는 기능을 제공한다.
  - 예를 들어서 등록시에 검증할 기능과 수정시에 검증할 기능을 각각 그룹으로 나누어 적용할 수 있다.
- 참고
  - @Valid 에는 groups를 적용할 수 있는 기능이 없다. 따라서 groups를 사용하려면 @Validated 를 사용해야 한다.

- 정리
  - groups 기능을 사용해서 등록과 수정시에 각각 다르게 검증을 할 수 있었다. 
    - 그런데 groups 기능을 사용하니 Item 은 물론이고, 전반적으로 복잡도가 올라갔다.
  - 사실 groups 기능은 실제 잘 사용되지는 않는데, 그 이유는 실무에서는 주로 다음에 등장하는 등록용 폼 객체와 수정용 폼 객체를 분리해서 사용하기 때문이다.

## Form 전송 객체 분리
- ValidationItemV4Controller
  - 실무에서는 groups 를 잘 사용하지 않는데, 그 이유가 다른 곳에 있다. 바로 등록시 폼에서 전달하는 데이터가 Item 도메인 객체와 딱 맞지 않기 때문이다.
  - 소위 "Hello World" 예제에서는 폼에서 전달하는 데이터와 Item 도메인 객체가 딱 맞는다. 
  - 하지만 실무에서는 회원 등록시 회원과 관련된 데이터만 전달받는 것이 아니라, 약관 정보도 추가로 받는 등 Item 과 관계없는 수 많은 부가 데이터가 넘어온다.
  - 그래서 보통 Item 을 직접 전달받는 것이 아니라, 복잡한 폼의 데이터를 컨트롤러까지 전달할 별도의 객체를 만들어서 전달한다. 
    - 예를 들면 ItemSaveForm 이라는 폼을 전달받는 전용 객체를 만들어서 @ModelAttribute 로 사용한다.
    - 이것을 통해 컨트롤러에서 폼 데이터를 전달 받고, 이후 컨트롤러에서 필요한 데이터를 사용해서 Item 을 생성한다.

- 폼 데이터 전달에 Item 도메인 객체 사용
  - HTML Form -> Item -> Controller -> Item -> Repository
  - 장점: Item 도메인 객체를 컨트롤러, 리포지토리 까지 직접 전달해서 중간에 Item을 만드는 과정이 없어서 간단하다.
  - 단점: 간단한 경우에만 적용할 수 있다. 수정시 검증이 중복될 수 있고, groups를 사용해야 한다.
- 폼 데이터 전달을 위한 별도의 객체 사용
  - HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository
  - 장점: 전송하는 폼 데이터가 복잡해도 거기에 맞춘 별도의 폼 객체를 사용해서 데이터를 전달 받을 수 있다. 
    - 보통 등록과, 수정용으로 별도의 폼 객체를 만들기 때문에 검증이 중복되지 않는다.
  - 단점: 폼 데이터를 기반으로 컨트롤러에서 Item 객체를 생성하는 변환 과정이 추가된다.
  
- 실제 수정과 등록 폼에서 다루는 데이터 범위가 다르고 데이터가 방대하기 때문에 별도의 객체로 데이터를 전달받는 것이 좋다.
- Q: 등록, 수정용 뷰 템플릿이 비슷한데 합치는게 좋을까요?
  - 뷰 템플릿 파일을 등록과 수정을 합치는게 좋을지 고민이 될 수 있다. 각각 장단점이 있으므로 고민하는게 좋지만, 
    - 어설프게 합치면 수 많은 분기문(등록일 때, 수정일 때) 때문에 나중에 유지보수에서 고통을 맛본다.
    - 이런 어설픈 분기문들이 보이기 시작하면 분리해야 할 신호이다


#### 폼 객체 바인딩 
```java
@PostMapping("/add")
public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
  //...
}
```
- Item 대신에 ItemSaveform 을 전달 받는다. 그리고 @Validated 로 검증도 수행하고, BindingResult 로 검증 결과도 받는다.

- 주의
  - @ModelAttribute("item") 에 item 이름을 넣어준 부분을 주의하자. 
  - 이것을 넣지 않으면 ItemSaveForm 의 경우 규칙에 의해 itemSaveForm 이라는 이름으로 MVC Model에 담기게 된다. 
  - 이렇게 되면 뷰 템플릿에서 접근하는 th:object 이름도 함께 변경해주어야 한다.
  
#### 폼 객체를 Item으로 변환 
```java
  //성공 로직
  Item item = new Item();
  item.setItemName(form.getItemName());
  item.setPrice(form.getPrice());
  item.setQuantity(form.getQuantity());
  Item savedItem = itemRepository.save(item); 
```
- 폼 객체의 데이터를 기반으로 Item 객체를 생성한다. 
  - 이렇게 폼 객체 처럼 중간에 다른 객체가 추가되면 변환하는 과정이 추가된다.