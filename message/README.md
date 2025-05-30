# 메시지, 국제화

## 메시지, 국제화 소개

- 메시지
  - 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.
    - 예를 들어서 message.properties라는 메시지 관리용 파일을 만들고 각 HTML들은 해당 데이터를 key 값으로 불러서 사용한다.
      - message.properties
        ```html
          item=상품
          item.id=상품 ID
          item.itemName=상품명
          item.price=가격
          item.quantity=수량
        ```
      - 다음과 같이 HTML에서 불러와서 사용
        - <label for="itemName" th:text="#{item.itemName}"></label>

- 국제화
  - 메시지에서 설명한 메시지 파일( messages.properties )을 각 나라별로 별도로 관리하면 서비스를 국제화 할 수 있다.
  - 예시
    - messages_en.properties
      ```properties
      item=Item
      item.id=Item ID
      item.itemName=Item Name
      item.price=price
      item.quantity=quantity
      ```
    - messages_ko.properties
      ```
      item=상품
      item.id=상품 ID
      item.itemName=상품명
      item.price=가격
      item.quantity=수량
      ```
      
## 스프링 메시지 소스 설정
- 스프링은 기본적인 메시지 관리 기능을 제공한다.
- 메시지 관리 기능을 사용하려면 스프링이 제공하는 MessageSource 를 스프링 빈으로 등록하면 되는데, 스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록한다.

- 스프링 부트 메시지 소스 설정
  - 스프링 부트를 사용하면 다음과 같이 메시지 소스를 설정할 수 있다.
  - application.properties
  ```
  spring.messages.basename=messages,config.i18n.messages
  ```
  - 스프링 부트 메시지 소스 기본 값
    - spring.messages.basename=messages
  - MessageSource 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 messages 라는 이름으로 기본 등록된다. 따라서 messages_en.properties , messages_ko.properties ,
    - messages.properties 파일만 등록하면 자동으로 인식된다

- 타임리프 메시지 적용
  - 타임리프의 메시지 표현식 #{...} 를 사용하면 스프링의 메시지를 편리하게 조회할 수 있다.
  - 예를 들어서 방금 등록한 상품이라는 이름을 조회하려면 #{label.item} 이라고 하면 된다.

- html에서 메시지 표현식을 이용해 코드를 만들어 놓으면 따로 건드릴 필요 없이 언어 우선순위를 변경하면 그 우선 순위에 맞게 언어 설정된다.
  -  브라우저의 언어 설정 값을 변경하면 요청시 Accept-Language 의 값이 변경된다.
    - Accept-Language 는 클라이언트가 서버에 기대하는 언어 정보를 담아서 요청하는 HTTP 요청 헤더이다.
    - 이 메시지 기능은 Locale 정보를 알아야 언어를 선택할 수 있다.
      - 결국 스프링도 Locale 정보를 알아야 언어를 선택할 수 있는데, 스프링은 언어 선택시 기본으로 Accept-Language 헤더의 값을 사용한다.

- LocaleResolver
  - 스프링은 Locale 선택 방식을 변경할 수 있도록 LocaleResolver 라는 인터페이스를 제공하는데, 
    - 스프링 부트는 기본으로 Accept-Language 를 활용하는 AcceptHeaderLocaleResolver 를 사용한다.
      ```html
        public interface LocaleResolver {
          Locale resolveLocale(HttpServletRequest request);
          void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale);
        }
      ```

- LocaleResolver 변경
  - 만약 Locale 선택 방식을 변경하려면 LocaleResolver 의 구현체를 변경해서 쿠키나 세션 기반의 Locale 선택 기능을 사용할 수 있다. 
  - 예를 들어서 고객이 직접 Locale 을 선택하도록 하는 것이다. 
  - 관련해서 LocaleResolver 를 검색하면 수 많은 예제가 나오니 필요하며 참고하면 된다.





