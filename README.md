# spring basic


### 단축키
- ctrl + shift + t /(command + shift + t)
  - test 코드 바로 만들기 단축키
- alt + enter /(option + enter)  
  - show context actions
- ctrl + alt + m / (command + alt + m)
  - extract/introduce -> extract method via

## 내용 정리

### 애자일 선언
- https://agilemanifesto.org/iso/ko/manifesto.html

### AppConfig
- 애플리케이션의 전체 동작 방식을 구성하기 위해, 구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스


---
### IoC
- 제어의 역전(Inversion Of Control)
  - 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 의미한다.

#### 프레임워크 vs 라이브러리
- 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크가 맞다. (ex - JUnit)
- 반면에 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 프레임워크가 아니라 라이브러리다.

### 의존관계 주입 DI(Dependency Injection)
- 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결 되는 것을 의존관계 주입이라 한다.

### IoC 컨테이너, DI 컨테이너
- AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을
- IoC 컨테이너 또는 DI 컨테이너라 한다.
  - IoC란 단어는 여러 군데에서 일어나는 범용적인 것으로, dependency Injection을 잘 해주는 애다라고 해서 DI 컨테이너로 이름을 바꿈.
- 의존관계 주입에 초점을 맞추어 최근에는 주로 DI 컨테이너라 한다.
- 또는 어샘블러, 오브젝트 팩토리 등으로 불리기도 한다.
  - 조립을 한다고 해서 어셈블러, 오브젝트를 만들어 낸다고 해서 오브젝트 팩토리라고 부르기도 함.
---
### 스프링 문법
- @Configuration
  - import org.springframework.context.annotation.Configuration;
  - 설정 정보를 담당한다는 의미, 설정을 구성한다는 뜻.
  - 파일의 앞에 적어주는 역할.
- @Bean
  - import org.springframework.context.annotation.Bean;
  - 메서드에 붙이면 스프링 컨테이너라는 곳에 들어감.
  - 메서드가 반환하는 객체를 스프링 컨테이너(ApplicationContext) 에 빈으로 등록한다.(스프링 컨테이너에 스프링 빈을 등록하는 것.)
  - 이렇게 등록된 빈은 스프링 컨테이너가 관리하고 다른 빈에서 의존성 주입(DI)을 통해 사용할 수 있다.

- 스프링 컨테이너
  - ApplicationContext 를 스프링 컨테이너라 한다.
  - 기존에는 개발자가 AppConfig 를 사용해서 직접 객체를 생성하고 DI를 했지만, 이제부터는 스프링 컨테이너를 통해서 사용한다.
  - 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용한다. 여기서 @Bean 이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 
    - 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
  - 스프링 빈은 @Bean 이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다. ( memberService , orderService)
  - 이전에는 개발자가 필요한 객체를 AppConfig 를 사용해서 직접 조회했지만, 이제부터는 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아야 한다. 
    - 스프링 빈은 applicationContext.getBean() 메서드를 사용 해서 찾을 수 있다.
  - 기존에는 개발자가 직접 자바코드로 모든 것을 했다면 이제부터는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고, 스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경되었다.
  - 코드가 약간 더 복잡해진 것 같은데, 스프링 컨테이너를 사용하면 어떤 장점이 있을까?
    - 개발 엔터프라이즈에서 개발해보면 스프링 컨테이너에서 관리함으로써 해줄 수 있는 기능이 어마어마하게 많다.
---

## Test 부분 개념

- @DisplayName : junit에서 한글로 이름을 작성 가능하게 함.