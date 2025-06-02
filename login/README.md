# 로그인 처리1 - 쿠키, 세션

#### 패키지 구조 설계
- package 구조
  - hello.login
    - domain
      - item
      - member
      - login
    - web
      - item
      - member
      - login
- 도메인이 가장 중요하다.
  - 도메인 = 화면, UI, 기술 인프라 등등의 영역은 제외한 시스템이 구현해야 하는 핵심 비즈니스 업무 영역을 말함
  - 향후 web을 다른 기술로 바꾸어도 도메인은 그대로 유지할 수 있어야 한다.
    - 이렇게 하려면 web은 domain을 알고있지만 domain은 web을 모르도록 설계해야 한다. 이것을 web은 domain을 의존하지만, domain은 web을 의존하지 않는다고 표현한다.

- 로그인
  - 로그인 컨트롤러는 로그인 서비스를 호출해서 로그인에 성공하면 홈 화면으로 이동하고, 
  - 로그인에 실패하면 bindingResult.reject() 를 사용해서 글로벌 오류( ObjectError )를 생성한다. 
  - 그리고 정보를 다시 입력하도록 로그인 폼을 뷰 템플릿으로 사용한다.