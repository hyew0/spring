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
      
