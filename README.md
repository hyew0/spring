# spring basic


## 단축키
- ctrl + shift + t /(command + shift + t)
  - test 코드 바로 만들기 단축키
- alt + enter /(option + enter)  
  - show context actions
- ctrl + alt + m / (command + alt + m)
  - extract/introduce -> extract method via

## Test 부분 개념

- @DisplayName : junit에서 한글로 이름을 작성 가능하게 함.

## 내용 정리

### 1. 애자일 선언
- https://agilemanifesto.org/iso/ko/manifesto.html

---

### 2. AppConfig
- 애플리케이션의 전체 동작 방식을 구성하기 위해, 구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스

---
### 3. IoC / DI / 컨테이너

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

### 4. 스프링 문법
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

### 5. 스프링 컨테이너와 스프링 빈

#### 스프링 컨테이너 생성
```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```
  - ApplicationContext를 스프링 컨테이너이며 인터페이스이다.
  - 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있다.
    - 요즘에는 애노테이션 기반을 주로 사용.
    - 에 AppConfig 를 사용했던 방식이 애노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것이다.
  - 자바 설정 클래스를 기반으로 스프링 컨테이너( ApplicationContext )를 만들어보자.
    - new AnnotationConfigApplicationContext(AppConfig.class);
    - 이 클래스는 ApplicationContext 인터페이스의 구현체이다.
  - 스프링 컨테이너를 부를 때 BeanFactory , ApplicationContext 로 구분해서 이야기한다. 
    -  BeanFactory 를 직접 사용하는 경우는 거의 없으므로 일반적으로 ApplicationContext 를 스프링 컨테이너라 한다.
#### 스프링 컨테이너 생성 과정
  1. 스프링 컨테이너 생성 - AppConfig.class
     - 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다.
     - 여기서는 AppConfig를 구성 정보롤 지정했음.
  2. 스프링 빈 등록
     - 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다.
     - 빈 이름
       - 빈 이름은 메서드 이름을 사용하고, 직접 지정할 수 도 있다.
         - @Bean(name="memberService2")
  3. 스프링 빈 의존관계 설정 
    - 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.
  - 스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나뉘어져 있다. 그러나 자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다.

#### 모든 빈 출력하기( ApplicationContextInfoTest )
  - 실행하면 스프링에 등록된 모든 빈 정보를 출력할 수 있다.
  - ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회한다.
  - ac.getBean() : 빈 이름으로 빈 객체(인스턴스)를 조회한다.
#### 애플리케이션 빈 출력하기
  - 스프링이 내부에서 사용하는 빈은 제외하고, 내가 등록한 빈만 출력해보자.
  - 스프링이 내부에서 사용하는 빈은 getRole() 로 구분할 수 있다.
  - ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
  - ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈

#### 스프링 빈 조회
  - 스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법
    - ac.getBean(빈이름, 타입)
    - ac.getBean(타입)
    - 조회 대상 스프링 빈이 없으면 예외 발생
      - NoSuchBeanDefinitionException: No bean named 'xxxxx' available

#### 스프링 빈 조회 - 동일한 타입이 둘 이상일 경우
  - 타입을 조회할 때 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생한다. 이때는 빈 이름을 지정하여 해결한다.
  - ac.getBeansOfType() 을 사용하면 해당 타입의 모든 빈을 조회할 수 있다.

#### 스프링 빈 조회 - 상속관계
- 부모 타입으로 조회하면 자식 타입도 함께 조회된다.
- 모든 자바 객체의 최고 부모인 Object 타입으로 조회 시 모든 스프링 빈을 조회한다.

#### BeanFactory와 ApplicationContext
- BeanFactory
  - 스프링 컨테이너의 최상위 인터페이스이며 스프링 빈을 관리하고 조회하는 역할을 담당한다.
  - getBean()을 제공
  - 지금까지 이전 커밋들에서 사용한 기능들은 BeanFactory가 제공하는 기능임.
- ApplicationContext
  - BeanFactory 기능을 모두 상속받아서 제공한다
  - 애플리케이션을 개발할 때는 빈을 관리하고 조회하는 기능은 물론이고 수많은 부가기능이 필요하다. 
    - BeanFactory 상속과 동시에 여러가지 다른 인터페이스들을 사용 가능.
      - MessageSource
        - 메세지 소스를 활용한 국제화 기능
          - 한국에 들어오면 한국어, 영어권이면 영어로 출력 이런식으로 동작
      - EnvironmentCapable
        - 환경변수
          - 로컬, 개발, 운영등을 구분해서 처리
      - ApplicationEventPublisher
        - 애플리케이션 이벤트
          - 이벤트를 발행하고 구독하는 모델을 편리하게 지원
      - ResourceLoader
        - 편리한 리소스 조회
          - 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회
- 정리
  - ApplicationContext는 BeanFactory의 기능을 상속받는다.
  - ApplicationContext는 빈 관리기능 + 편리한 부가 기능을 제공한다.
  - BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된 ApplicationContext를 사용한다.
  - BeanFactory나 ApplicationContext를 스프링 컨테이너라 한다.
  
#### 다양한 설정 형식 지원 - 자바 코드, XML
- 스프링 컨테이너는 다양한 형식의 설정 정보를 받아들일 수 있게 유연하게 설계되어 있다.
  - 자바 코드, XML, Groovy 등등
- 애노테이션 기반 자바 코드 설정 사용
  - 이전 커밋까지 사용했던 것이다.
  - new AnnotationConfigApplicationContext(AppConfig.class)
  - AnnotationConfigApplicationContext 클래스를 사용하면서 자바 코드로된 설정 정보를 넘기면 된다.
- XML 설정 사용
  - 최근에는 스프링 부트를 많이 사용하면서 XML기반의 설정은 잘 사용하지 않는다. 
    - 아직 많은 레거시 프로젝트 들이 XML로 되어 있고, 또 XML을 사용하면 컴파일 없이 빈 설정 정보를 변경할 수 있는 장점도 있으므로 한번쯤 배워두는 것도 괜찮다.
  - GenericXmlApplicationContext 를 사용하면서 xml 설정 파일을 넘기면 된다.

#### 스프링 빈 설정 메타 정보 - BeanDefinition
- 스프링은 어떻게 이런 다양한 설정 형식을 지원하는 것일까? 그 중심에는 BeanDefinition 이라는 추상화가 있다.
  - 쉽게 이야기해서 역할과 구현을 개념적으로 나눈 것.
    - XML을 읽어서 BeanDefinition을 만들면 된다.
    - 자바 코드를 읽어서 BeanDefinition을 만들면 된다.
    - 스프링 컨테이너는 자바 코드인지, XML인지 몰라도 된다. 오직 BeanDefinition만 알면 된다.
  - BeanDefinition 을 빈 설정 메타정보라 한다.
    - @Bean , <bean> 당 각각 하나씩 메타 정보가 생성된다.
  - 스프링 컨테이너는 이 메타정보를 기반으로 스프링 빈을 생성한다.

- 실제 동작 방식
  - 자바 코드: AnnotationConfigApplicationContext 는 AnnotatedBeanDefinitionReader 를 사용해서 AppConfig.class 를 읽고 BeanDefinition 을 생성한다.
  - xml: GenericXmlApplicationContext 는 XmlBeanDefinitionReader 를 사용해서 appConfig.xml 설정 정보를 읽고 BeanDefinition 을 생성한다.
  - 새로운 형식의 설정 정보가 추가되면, XxxBeanDefinitionReader를 만들어서 BeanDefinition 을 생성하면 된다.

- BeanDefinition 정보
  - BeanClassName: 생성할 빈의 클래스 명(자바 설정 처럼 팩토리 역할의 빈을 사용하면 없음)
  - factoryBeanName: 팩토리 역할의 빈을 사용할 경우 이름, 예) appConfig
  - factoryMethodName: 빈을 생성할 팩토리 메서드 지정, 예) memberService
  - Scope: 싱글톤(기본값)
  - lazyInit: 스프링 컨테이너를 생성할 때 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때 까지 최대한 생성을 지연처리 하는지 여부
  - InitMethodName: 빈을 생성하고, 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명
  - DestroyMethodName: 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명
  - Constructor arguments, Properties: 의존관계 주입에서 사용한다. (자바 설정 처럼 팩토리 역할의 빈을 사용하면 없음)

- 정리
  - BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수 도 있다. 하지만 실무에서 BeanDefinition을 직접 정의하거나 사용할 일은 거의 없다.
  - BeanDefinition에 대해서는 너무 깊이있게 이해하기 보다는, 스프링이 다양한 형태의 설정 정보를 BeanDefinition으로 추상화해서 사용하는 것 정도만 이해하면 된다.
  - 가끔 스프링 코드나 스프링 관련 오픈 소스의 코드를 볼 때, BeanDefinition 이라는 것이 보일 때가 있다. 이때 이러한 메커니즘을 떠올리면 된다.
---

#### 싱글톤 컨테이너

- 웹 애플리케이션과 싱글톤
  - 웹 애플리케이션은 보통 여러 고객이 동시에 요청한다.
    - 이전까지 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청할 때마다 객체를 새로 생성한다.
    - 객체가 매번 새로 생성되면 메모리 낭비가 심해지기 때문에 "싱글톤 패턴"을 이용하여 해당 객체를 1개만 생성 후 공유하도록 설계한다.

- 싱글톤 패턴
  - 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴.
  - 객체 인스턴스가 2개 이상 생성되지 못하도록 막아야 하는데, 
    - 순수한 DI 컨테이너에서는 private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막는다.
  - 싱글톤 패턴의 문제점
    - 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
    - 의존관계상 클라이언트가 구체 클래스에 의존한다. DIP를 위반한다.
    - 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
    - 테스트하기 어렵다.
    - 내부 속성을 변경하거나 초기화 하기 어렵다.
    - private 생성자로 자식 클래스를 만들기 어렵다.
    - 결론적으로 유연성이 떨어진다.
    - 안티패턴으로 불리기도 한다.
