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

## 컨버전 서비스 - ConversionService
- 스프링은 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공한다.
  - 이것이 바로 컨버전 서비스(ConversionService)이다.
- ConversionService 인터페이스
  - ```
    package org.springframework.core.convert;
    import org.springframework.lang.Nullable;
    
    public interface ConversionService {
      boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);
      boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
      
      <T> T convert(@Nullable Object source, Class<T> targetType);
      Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
    }
    ```
  - 컨버전 서비스 인터페이스는 단순히 컨버팅이 가능한가? 확인하는 기능과, 컨버팅 기능을 제공한다.

## 스프링에 Converter 적용하기
- 스프링은 내부에서 ConversionService 를 제공한다. 
  - 우리는 WebMvcConfigurer 가 제공하는 addFormatters() 를 사용해서 추가하고 싶은 컨버터를 등록하면 된다. 
  - 이렇게 하면 스프링은 내부에서 사용하는 ConversionService 에 컨버터를 추가해준다.

## 뷰 템플릿에 컨버터 적용하기
-  템플릿에 컨버터를 적용하는 방법을 알아보자.
   - 타임리프는 렌더링 시에 컨버터를 적용해서 렌더링 하는 방법을 편리하게 지원한다.
   - 이전까지는 문자를 객체로 변환했다면, 이번에는 그 반대로 객체를 문자로 변환하는 작업을 확인할 수 있다.

- 타임리프는 ${{...}} 를 사용하면 자동으로 컨버전 서비스를 사용해서 변환된 결과를 출력해준다. 
  - 물론 스프링과 통합 되어서 스프링이 제공하는 컨버전 서비스를 사용하므로, 우리가 등록한 컨버터들을 사용할 수 있다.

- 변수 표현식 : ${...}
- 컨버전 서비스 적용 : ${{...}}

## 포맷터 - Formatter
- Converter 는 입력과 출력 타입에 제한이 없는, 범용 타입 변환 기능을 제공한다.
- 일반적인 웹 애플리케이션 환경을 생각해보자. 
  - 불린 타입을 숫자로 바꾸는 것 같은 범용 기능 보다는 개발자 입장에서는 문자를 다른 타입으로 변환하거나, 다른 타입을 문자로 변환하는 상황이 대부분이다.
  - 웹 애플리케이션에서 객체를 문자로, 문자를 객체로 변환하는 예
    - 화면에 숫자를 출력해야 하는데, Integer String 출력 시점에 숫자 1000 문자 "1,000" 이렇게 1000 단위에 쉼표를 넣어서 출력하거나, 또는 "1,000" 라는 문자를 1000 이라는 숫자로 변경해야 한다.
    - 날짜 객체를 문자인 "2021-01-01 10:50:11" 와 같이 출력하거나 또는 그 반대의 상황
- Locale
  - 날짜 숫자의 표현 방법은 Locale 현지화 정보가 사용될 수 있다.
- 이렇게 객체를 특정한 포맷에 맞추어 문자로 출력학나 또는 그 반대의 역할에 하는 것에 특화된 기능이 포맷터(Formatter)이다.
  - 포맷터는 컨버터의 특별한 버전으로 이해하면 된다.
  - Converter vs Formatter
    - Converter 는 범용(객체 객체)
    - Formatter 는 문자에 특화(객체 문자, 문자 객체) + 현지화(Locale)
      - Converter 의 특별한 버전
- Formatter  인터페이스
  - ```java
      public interface Printer<T> {
        String print(T object, Locale locale);
      }
  
      public interface Parser<T> {
        T parse(String text, Locale locale) throws ParseException;
      }
    
      public interface Formatter<T> extends Printer<T>, Parser<T> {
      }
    ```
    
## 포맷터를 지원하는 컨버전 서비스
- 컨버전 서비스에는 컨버터만 등록할 수 있고, 포맷터를 등록할 수 는 없다. 
  - 그런데 생각해보면 포맷터는 객체 -> 문자, 문자 -> 객체로 변환하는 특별한 컨버터일 뿐이다.
- 포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다. 
  - 내부에서 어댑터 패턴을 사용해서 Formatter 가 Converter 처럼 동작하도록 지원한다.
- FormattingConversionService 는 포맷터를 지원하는 컨버전 서비스이다.
  - DefaultFormattingConversionService 는 FormattingConversionService 에 기본적인 통화, 숫자 관련 몇가지 기본 포맷터를 추가해서 제공한다.

- DefaultFormattingConversionService 상속 관계
  - FormattingConversionService 는 ConversionService 관련 기능을 상속받기 때문에 결과적으로 컨버터도 포맷터도 모두 등록할 수 있다. 
  - 그리고 사용할 때는 ConversionService 가 제공하는 convert 를 사용하면 된다.
  - 추가로 스프링 부트는 DefaultFormattingConversionService 를 상속 받은 WebConversionService 를 내부에서 사용한다.

## 포맷터 적용하기
