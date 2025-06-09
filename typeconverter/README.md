# 스프링 타입 컨버터

## 스프링 타입 컨버터 소개
- 문자를 숫자로 변환하거나, 반대로 숫자를 문자로 변환해야 하는 것처럼 애플리케이션을 개발하다보면 타입을 변환해야 하는 경우가 많다.

- HTTP 요청 파라미터는 모두 문자로 처리된다. 
  - 따라서 요청 파라미터를 자바에서 다른 타입으로 변환해서 사용하고 싶으면 다음과 같이 숫자 타입으로 변환하는 과정을 거쳐야 한다.

- @RequestParam
  - ```
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
      System.out.println("data = " + data);
      return "ok";
    }    
    ```
    - @RequstParam을 사용하면 문자를 숫자로 편리하게 변환 가능.

- @ModelAttribute 
  - @ModelAttribute 타입 변환 예시 
    - ```
      @ModelAttribute UserData data
      class UserData {
        Integer data;
      }
      ```
    - @RequestParam 와 같이, 문자 data=10 을 숫자 10으로 받을 수 있다.

- @PathVariable
  - @PathVariable 타입 변환 예시 
    - ```
      /users/{userId}
      @PathVariable("userId") Integer data 
      ```
      - URL 경로는 문자다. /users/10 
        - 여기서 10도 숫자 10이 아니라 그냥 문자 "10"이다. 
        - data를 Integer 타입으로 받을 수 있는 것도 스프링이 타입 변환을 해주기 때문이다.

- 스프링의 타입 변환 적용 예
  - 스프링 MVC 요청 파라미터
    - @RequestParam , @ModelAttribute , @PathVariable
  - @Value 등으로 YML 정보 읽기
  - XML에 넣은 스프링 빈 정보를 변환
  - 뷰를 렌더링 할 때

- 새로운 타입을 만들어서 변환하고 싶으면 컨버터 인테이스 사용
  - 컨버터 인터페이스 
    - ```java
      package org.springframework.core.convert.converter;
      
      public interface Converter<S, T> {
        T convert(S source);
      }
      ```
  - 스프링은 확장 가능한 컨버터 인터페이스를 제공한다.
    - 개발자는 스프링에 추가적인 변환 타입 필요 시 컨버터 인터페이스를 구현해서 등록하면 된다.

## 타입 컨버터 - Converter
- 타입 컨버터를 사용하려면 org.springframework.core.convert.converter.Converter 인터페이스를 구현하면 된다.
- 주의
  - Converter 라는 이름의 인터페이스가 많으니 조심해야 한다.
  - org.springframework.core.convert.converter.Converter 를 사용해야 한다.
- 컨버터 인터페이스 
  - ```java
    package org.springframework.core.convert.converter;
    
    public interface Converter<S, T> {
      T convert(S source);
    }
    ```
    
- 롬복의 @EqualsAndHashCode 를 넣으면 모든 필드를 사용해서 equals() , hashcode() 를 생성한다. 
  - 따라서 모든 필드의 값이 같다면 a.equals(b) 의 결과가 참이 된다.

- 참고
  - 스프링은 용도에 따라 다양한 방식의 타입 컨버터를 제공한다.
    - Converter 기본 타입 컨버터
    - ConverterFactory 전체 클래스 계층 구조가 필요할 때
    - GenericConverter 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
    - ConditionalGenericConverter 특정 조건이 참인 경우에만 실행
    - 자세한 내용은 공식 문서를 참고하자.
      - https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#core-convert
  - 스프링은 문자, 숫자, 불린, Enum등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다. 
    - IDE에서 Converter , ConverterFactory , GenericConverter 의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.