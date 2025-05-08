# spring basic


## 단축키
- ctrl + shift + t /(command + shift + t)
  - test 코드 바로 만들기 단축키
- alt + enter /(option + enter)  
  - show context actions
- ctrl + alt + m / (command + alt + m)
  - extract/introduce -> extract method via
- shift + shift 
  - 검색창 열기

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

###  6. 싱글톤 컨테이너

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

- 싱글톤 컨테이너
  - 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
  - 지금까지의 커밋한 스프링 빈이 싱글톤으로 관리되는 빈이다.
  - 싱글톤 컨테이너의 정리
    - 스프링 컨테이너는 싱글톤 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
    - 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 한다.
    - 스프링 컨테이너의 이런 기능 덕분에 싱글톤 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
      - 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
      - DIP,OCP, 테스트, private 생성자로부터 자유롭게 싱글톤을 사용할 수 있다.
  - 참고) 스프링의 기본 빈 등록 방식은 싱글톤이지만, 요청할 때 마다 새로운 객체를 생성해서 반환하는 기능도 제공한다.

- 싱글톤 방식의 주의점
  - 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 
    - 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
  - 무상태(stateless)로 설계해야 한다.
    - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
    - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
    - 가급적 읽기만 가능해야 한다.
    - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
  - 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다!!!

- @Configuration과 바이트코드 조작의 마법
  - 스프링 컨테이너는 싱글톤 레지스트리다. 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야 한다. 
    - 그런데 스프링이 자바 코드까지 어떻게 하기는 어렵다. AppConfig 자바 코드를 보면 분명 3번 호출되어야 하는 것이 맞다.
    - 그래서 스프링은 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.
    - AnnotationConfigApplicationContext 에 파라미터로 넘긴 값은 스프링 빈으로 등록된다. 그래서 AppConfig 도 스프링 빈이 된다.
  - AppConfig 스프링 빈 조회
    - bean = class hello.core.AppConfig$$SpringCGLIB$$0
    - 순수한 클래스라면 ->  class hello.core.AppConfig 이렇게 나오는 것이 맞다.
      - 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들어서 스프링 빈으로 등록했기 때문에 뒤에 저런 것이 붙는 것이다..
    - 이렇게 만든 임의의 클래스가 싱글톤을 보장해주는 것이다.
      - @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
        - 덕분에 싱글톤이 보장되는 것이다.
    - 참고)  AppConfig@CGLIB는 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 할 수 있다.
  - @Configuration을 적용하지 않고, @Bean만 적용한다면?
    - @Configuration 을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장하지만, 만약 @Bean만 적용하면 어떻게 될까?
      - 순수한 클래스인 -> class hello.core.AppConfig 출력

##### 정리

- @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
- memberRepository() 처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
- 스프링 설정 정보는 항상 @Configuration 을 붙여주도록 한다. (그냥 이렇게 알고 있으면 된다!)

---

### 7. 컴포넌트 스캔

- 컴포넌트 스캔은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 기능이다.
- 의존관계도 자동으로 주입하는 @Autowired 라는 기능도 제공한다.

- 현재 코드 참고(AutoAppConfig.java)
  - 참고) 컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에, AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 
    - 그래서 excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다. 
    - 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다

- 컴포넌트 스캔은 @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다.
- 참고) @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 애노테이션이 붙어있기 때문이다.

##### 컴포넌트 스캔과 자동 의존관계 주입 동작 과정
1. @ComponentScan
- @ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다.
  - 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
    - 빈 이름 기본 전략: MemberServiceImpl 클래스 memberServiceImpl
    - 빈 이름 직접 지정: 만약 스프링 빈의 이름을 직접 지정하고 싶으면
      - @Component("memberService2") 이런식으로 이름을 부여하면 된다.
2. @Autowired 의존관계 자동 주입
- 생성자에 @Autowired 를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.
- 이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다.
  - getBean(MemberRepository.class) 와 동일하다고 이해하면 된다.
- 생성자에 파라미터가 많아도 알아서 찾아서 자동으로 주입한다.

##### 탐색 위치와 기본 스캔 대상
  - 탐색 위치는 시작 위치를 지정할 수 있다.
  ```
  @ComponentScan(
    basePackages = "hello.core",
  }
  ```
  - basePackages : 탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
    - 여러 시작 위치를 지정하는 것도 가능하다.
      - basePackages = {"hello.core", "hello.service"} 
    - basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정한다.
    - 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.
  - 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것을 추천하고, 스프링 부트도 이 방법을 기본으로 제공하고 있다.
    - 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두는 것이 좋다.
    - 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다. (이 설정안에 바로 @ComponentScan 이 들어있다)

  - 컴포넌트 스캔 기본 대상
    - 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다.
      - @Component : 컴포넌트 스캔에서 사용
      - @Controller : 스프링 MVC 컨트롤러에서 사용
      - @Service : 스프링 비즈니스 로직에서 사용
      - @Repository : 스프링 데이터 접근 계층에서 사용
      - @Configuration : 스프링 설정 정보에서 사용
    - 참고) 
      - 애노테이션은 상속관계라는 것이 없기 때문에 애노테이션이 특정 애노테이션을 들고 있는 것을 인식할 수 있는 것은 자바가 아니라 스프링이 지원하는 기능.
    - 애노테이션 부가 기능
      - @Controller 
        - 스프링 MVC 컨트롤러로 인식
      - @Repository 
        - 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
      - @Configuration 
        - 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.
      - @Service 
        - @Service 는 특별한 처리를 하지 않는다. 대신 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.
    - 참고) 
      - useDefaultFilters 옵션은 기본으로 켜져있는데, 이 옵션을 끄면 기본 스캔 대상들이 제외된다. 그냥 이런 옵션이 있구나 정도 알면 됨.

##### 필터
  - 필터 2가지
    - includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다.
    - excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.
  - FilterType 옵션
    - 5가지 옵션
      - ANNOTATION: 
        - 기본값, 애노테이션을 인식해서 동작한다.
        - ex) org.example.SomeAnnotation
      - ASSIGNABLE_TYPE: 
        - 지정한 타입과 자식 타입을 인식해서 동작한다.
        - ex) org.example.SomeClass
      - ASPECTJ: 
        - AspectJ 패턴 사용
        - ex) org.example..*Service+
      - REGEX: 
        - 정규 표현식
        - ex) org\.example\.Default.*
      - CUSTOM: 
        - TypeFilter 이라는 인터페이스를 구현해서 처리
        - ex) org.example.MyTypeFilter
    - 참고)
      - @Component 면 충분하기 때문에, includeFilters 를 사용할 일은 거의 없다. 
      - excludeFilters 는 여러가지 이유로 간혹 사용할 때가 있지만 많지는 않다.
      - 특히 최근 스프링 부트는 컴포넌트 스캔을 기본으로 제공하는데, 옵션을 변경하면서 사용하기 보다는 스프링의 기본 설정에 최대한 맞추어 사용하는 것을 권장.

##### 중복 등록과 충돌
- 컴포넌트 스캔에서 같은 빈 등록으로서의 상황 2가지
  - 자동 빈 등록 vs 자동 빈 등록 
  - 수동 빈 등록 vs 자동 빈 등록

- 자동 빈 등록 vs 자동 빈 등록
  - 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 이름이 같은 경우 스프링은 오류를 발생시킨다.
    - ConflictingBeanDefinitionException 예외 발생

- 수동 빈 등록 vs 자동 빈 등록
  - 수동 빈 등록이 우선권을 가져서 수동 빈이 자동 빈을 오버라이딩한다.
  - 수동 등록 시 로그
    - Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing
  - 개발자가 의도적으로 이런 결과를 기대했다면 참 좋지만 대체로 설정들이 꼬여서 이런 결과가 나오는 경우가 많기 때문에 
    - 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌되면 오류가 발생하도록 기본 값을 변경하였다.
    - 수동 빈 등록, 자동 빈 등록 오류시 스프링 부트 에러
      - Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true

---

### 8. 의존관계 자동 주입

##### 다양한 의존관계 주입 방법
- 의존관계 주입 방법 4가지
1. 생성자 주입
2. 수정자 주입(setter 주입)
3. 필드 주입
4. 일반 메서드 주입

- 생성자 주입
  - 생성자를 통해서 의존 관계를 주입 받는 방법이다.
  - 특징
    - 생성자 호출 시점에 딱 한 번만 호출되는 것이 보장된다.
    - 불변, 필수 의존관계에 사용.
  - 생성자가 1개만 있으면 @Autowired를 생략해도 자동 주입된다.( 스프링 빈만 해당.)

```java
@Component
public class OrderServiceImpl implements OrderService {
 private final MemberRepository memberRepository;
 private final DiscountPolicy discountPolicy;
 
 @Autowired
 public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
   this.memberRepository = memberRepository;
   this.discountPolicy = discountPolicy;
 }
}
```

- 수정자 주입(setter 주입)
  - setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법이다.
  - 특징
    - 선택, 변경 가능성이 있는 의존관계에 사용
    - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.
  ```java
  @Component
  public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
    }
  
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
      this.discountPolicy = discountPolicy;
    }
  }
  ```
  - 참고)
    -  @Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false) 로 지정하면 된다.
    - 자바빈 프로퍼티, 자바에서는 과거부터 필드의 값을 직접 변경하지 않고, setXxx, getXxx 라는 메서드를 통해서 값을 읽거나 수정하는 규칙을 만들었는데, 그것이 자바빈 프로퍼티 규약이다.

    - 필드 주입
      - 이름 그대로 필드에 바로 주입하는 방법이다.
      - 특징
        - 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다.
        - DI 프레임워크가 없으면 아무것도 할 수 없다.
        - 사용하지 말자!
          - 애플리케이션의 실제 코드와 관계 없는 테스트 코드
          - 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용
          ```java
          @Component
          public class OrderServiceImpl implements OrderService {
            @Autowired
            private MemberRepository memberRepository;
            
            @Autowired
            private DiscountPolicy discountPolicy;
          }
          ```
      - 참고: 
        - 순수한 자바 테스트 코드에는 당연히 @Autowired가 동작하지 않는다. @SpringBootTest 처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능하다.
        - 다음 코드와 같이 @Bean 에서 파라미터에 의존관계는 자동 주입된다. 수동 등록시 자동 등록된 빈의 의존 관계가 필요할 때 문제를 해결할 수 있다.

  - 일반 메서드 주입
    - 일반 메서드를 통해서 주입 받을 수 있다.
    - 특징
      - 한번에 여러 필드를 주입 받을 수 있다.
      - 일반적으로 잘 사용하지 않는다.
    ```java
    @Component
    public class OrderServiceImpl implements OrderService {
      private MemberRepository memberRepository;
      private DiscountPolicy discountPolicy;
  
      @Autowired
      public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
      }
    }
    ```
    - 참고: 
      - 어쩌면 당연한 이야기이지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다.
      - 스프링 빈이 아닌 Member 같은 클래스에서 @Autowired 코드를 적용해도 아무 기능도 동작하지 않는다.

##### 옵션처리
- 주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
  - 그런데 @Autowired 만 사용하면 required 옵션의 기본값이 true 로 되어 있어서 자동 주입 대상이 없으면 오류가 발생한다.
- 자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다.
  - @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
  - org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
  - Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.
- 참고)
  - @Nullable, Optional은 스프링 전반에 걸쳐서 지원된다. 예를 들어서 생성자 자동 주입에서 특정 필드에만 사용해도 된다

##### 생성자 주입을 선택하라
- 과거에는 수정자 주입과 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다
- 이유
  - 불변
  - 누락
  - final 키워드

- 불변
  - 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 
    - 오히려 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안된다.(불변해야 한다.)
  - 수정자 주입을 사용하면, setXxx 메서드를 public으로 열어두어야 한다.
  - 누군가 실수로 변경할 수 도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
  - 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.

- 누락
  - 프레임워크 없이 순수한 자바 코드를 단위 테스트 하는 경우에 
  - 다음과 같이 수정자 의존관계인 경우 
  ```java
  public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
    }
    
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
      this.discountPolicy = discountPolicy;
    }
    //...
  }
  ```
  - @Autowired 가 프레임워크 안에서 동작할 때는 의존관계가 없으면 오류가 발생하지만, 지금은 프레임워크 없이 순수한 자바 코드로만 단위 테스트를 수행하고 있다.
  - 테스트를 실행하면 실행 결과가 NPE(Null Point Exception)이 발생하는데
    - memberRepository, discountPolicy 에서 의존관계 주입이 누락되었기 때문이다.
  - 생성자 주입을 사용하면 누락되었을 때 컴파일 오류가 발생하여 누락이 없도록 처리할 수 있게 된다.

- final 키워드
  - 생성자 주입을 사용하면 필드에 final 키워드를 사용할 수 있다. 그래서 생성자에서 혹시라도 값이 설정되지 않는 오
    류를 컴파일 시점에 막아준다.  
  ```java
  @Component
  public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
      this.memberRepository = memberRepository;
    }
    
    //...
  }
  ```
  - 잘 보면 필수 필드인 discountPolicy 에 값을 설정해야 하는데, 이 부분이 누락되었다. 
  - 자바는 컴파일 시점에 다음 오류를 발생시킨다.
    - java: variable discountPolicy might not have been initialized
    - 기억하자! 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!
  - 참고)
    - 수정자 주입을 포함한 나머지 주입 방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다. 
    - 오직 생성자 주입 방식만 final 키워드를 사용할 수 있다.

- 정리
  - 생성자 주입 방식을 선택하는 이유는 여러가지가 있지만, 
    - 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징 을 잘 살리는 방법이기도 하다.
  - 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다. 
    - 생성자 주입과 수정자 주입을 동시에 사용할 수 있다.
  - 항상 생성자 주입을 선택해라. 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 
    - 필드 주입은 사용하지 않는 게 좋다.

##### 롬복과 최신 트렌드

- 막상 개발을 해보면 대부분 불변, 필드에 final 키워드를 사용한다. 그런데 생성자 만들고, 대입 코드 만들고 등등 반복되는 작업을 해야한다.
  - 편리하게 사용하기 위해 Lombok을 사용한다!
  - 롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다. (다음 코드에는 보이지 않지만 실제 호출 가능하다.)

- 롬복 설치법
  - settings -> plugins -> lombok 검색해서 설치
  - settings -> annotation processing 체크
  - 다음과 같이 lombok 설정 부분을 build.gradle에 넣어주기 (아래는 예시로 본인의 gradle 에 설정 부분을 넣어주면됨.)
    - 프로젝트 생성 시 롬복을 선택할 수 도 있음. (지금 프로젝트는 선택 안했기 때문에 직접 넣어줌.)
      ```java
      plugins {
      id 'org.springframework.boot' version '2.3.2.RELEASE'
      id 'io.spring.dependency-management' version '1.0.9.RELEASE'
      id 'java'
      }
    
      group = 'hello'
      version = '0.0.1-SNAPSHOT'
      sourceCompatibility = '11'
    
      //lombok 설정 추가 시작
      configurations {
        compileOnly {
          extendsFrom annotationProcessor
        }
      }
      //lombok 설정 추가 끝
    
      repositories {
        mavenCentral()
      }
      dependencies {
        implementation 'org.springframework.boot:spring-boot-starter'
              
        //lombok 라이브러리 추가 시작
        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
        testCompileOnly 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'
        //lombok 라이브러리 추가 끝
      
        testImplementation('org.springframework.boot:spring-boot-starter-test') {
          exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
        }
      }
      test {
        useJUnitPlatform()
      }
      ```
  - 임의의 테스트 클래스를 만들고 @Getter, @Setter 확인

- 정리
  - 최근에는 생성자를 딱 1개 두고, @Autowired 를 생략하는 방법을 주로 사용한다. 
  - 여기에 Lombok 라이브러리의 @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드는 깔끔하게 사용할 수 있다.

##### 조회 빈이 2개 이상이 되면 발생하는 문제
- @Autowired 는 타입(Type)으로 조회한다.

- 타입으로 조회하기 때문에, 마치 다음 코드와 유사하게 동작한다. (실제로는 더 많은 기능을 제공한다.)
  - ac.getBean(DiscountPolicy.class)
  - 스프링 빈 조회에서 학습했듯이 타입으로 조회하면 선택된 빈이 2개 이상일 때 문제가 발생한다.
    - NoUniqueBeanDefinitionException 오류가 발생한다.
- 스프링 빈을 수동 등록해서 문제를 해결해도 되지만, 의존 관계 자동 주입에서 해결하는 여러 방법이 있다.

##### @Autowired 필드 명, @Qualifier, @Primary

- 조회 대상 빈이 2개 이상일 때 해결 방법 3가지
  - @Autowired 필드 명 매칭
  - @Qualifier @Qualifier끼리 매칭 빈 이름 매칭
  - @Primary 사용

- @Autowired 필드 명 매칭
  - @Autowired 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭한
    다.
    - 기존 코드
      ```
      @Autowired
      private DiscountPolicy discountPolicy
      ```
    - 필드 명을 빈 이름으로 변경
      ```
      @Autowired
      private DiscountPolicy rateDiscountPolicy
      ```
    - 필드 명이 rateDiscountPolicy 이므로 정상 주입된다.
  - 필드 명 매칭은 먼저 타입 매칭을 시도 하고 그 결과에 여러 빈이 있을 때 추가로 동작하는 기능이다.
  
  - @Autowired 매칭 정리
    1. 타입 매칭
    2. 타입 매칭의 결과가 2개 이상일 때 필드 명, 파라미터 명으로 빈 이름 매칭

- @Qualifier 사용
  - @Qualifier 는 추가 구분자를 붙여주는 방법이다. 
  - 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.

  - 빈 등록시 @Qualifier를 붙여 준다. 
  ```java
  @Component
  @Qualifier("mainDiscountPolicy")
  public class RateDiscountPolicy implements DiscountPolicy {} 
  ```
  ```java
  @Component
  @Qualifier("fixDiscountPolicy")
  public class FixDiscountPolicy implements DiscountPolicy {}
  ```
  - 주입시에 @Qualifier를 붙여주고 등록한 이름을 적어준다.

  - 생성자 자동 주입 예시 
  ```java
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository,
        @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
  ```
  - @Qualifier 로 주입할 때 @Qualifier("mainDiscountPolicy") 를 못찾으면 어떻게 될까? 
    - 그러면 mainDiscountPolicy라는 이름의 스프링 빈을 추가로 찾는다. 
    - 하지만 @Qualifier 는 @Qualifier 를 찾는 용도로만 사용하는게 명확하고 좋다.

  - @Qualifier 정리
    1. @Qualifier끼리 매칭
    2. 빈 이름 매칭
    3. NoSuchBeanDefinitionException 예외 발생

- @Primary 사용
  - @Primary 는 우선순위를 정하는 방법이다. 
    - @Autowired 시에 여러 빈이 매칭되면 @Primary 가 우선권을 가진다.

- 여기까지 보면 @Primary 와 @Qualifier 중에 어떤 것을 사용하면 좋을지 고민이 될 것이다. 
  - @Qualifier 의 단점은 주입 받을 때 모든 코드에 @Qualifier 를 붙여주어야 한다는 점이다.
  - 반면에 @Primary 를 사용하면 이렇게 @Qualifier 를 붙일 필요가 없다.

- @Primary, @Qualifier 활용
  - 코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈이 있고, 코드에서 특별한 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈이 있다고 생각해보자. 
  - 메인 데이터베이스의 커넥션을 획득하는 스프링 빈은 @Primary 를 적용해서 조회하는 곳에서 @Qualifier 지정 없이 편리하게 조회하고, 
  - 서브 데이터베이스 커넥션 빈을 획득할 때는 @Qualifier 를 지정해서 명시적으로 획득 하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있다. 
  - 물론 이때 메인 데이터베이스의 스프링 빈을 등록할 때 @Qualifier 를 지정해주는 것은 상관없다.
- 우선순위
  - @Primary 는 기본값 처럼 동작하는 것이고, @Qualifier 는 매우 상세하게 동작한다. 이런 경우 어떤 것이 우선권을 가져갈까? 
    - 스프링은 자동보다는 수동이, 넒은 범위의 선택권 보다는 좁은 범위의 선택권이 우선 순위가 높다. 따라서 여기서도 @Qualifier 가 우선권이 높다.

##### 자동, 수동의 올바른 실무 운영 기준

- 편리한 자동 기능을 기본으로 사용하자
  - 그러면 어떤 경우에 컴포넌트 스캔과 자동 주입을 사용하고, 어떤 경우에 설정 정보를 통해서 수동으로 빈을 등록하고,
  - 의존관계도 수동으로 주입해야 할까?   
    - 결론부터 이야기하면, 스프링이 나오고 시간이 갈 수록 점점 자동을 선호하는 추세다. 스프링은 @Component 뿐만 아니라 
      - @Controller , @Service , @Repository 처럼 계층에 맞추어 일반적인 애플리케이션 로직을 자동으로 스캔할 수 있도록 지원한다. 
      - 거기에 더해서 최근 스프링 부트는 컴포넌트 스캔을 기본으로 사용하고, 스프링 부트의 다양한 스프링 빈들도 조건이 맞으면 자동으로 등록하도록 설계했다.
  - 설정 정보를 기반으로 애플리케이션을 구성하는 부분과 실제 동작하는 부분을 명확하게 나누는 것이 이상적이지만, 
    - 개발자 입장에서 스프링 빈을 하나 등록할 때 @Component 만 넣어주면 끝나는 일을 @Configuration 설정 정보에 가서 @Bean 을 적고, 객체를 생성하고, 주입할 대상을 일일이 적어주는 과정은 상당히 번거롭다.
    - 또 관리할 빈이 많아서 설정 정보가 커지면 설정 정보를 관리하는 것 자체가 부담이 된다.
    - 그리고 결정적으로 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있다.

- 그러면 수동 빈 등록은 언제 사용하면 좋을까?
   - 애플리케이션은 크게 업무 로직과 기술 지원 로직으로 나눌 수 있다.
     - 업무 로직 빈: 
       - 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리등이 모두 업무 로직이다. 
       - 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
     - 기술 지원 빈: 
       - 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.

     - 업무 로직은 숫자도 매우 많고, 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리 처럼 어느정도 유사한 패턴이 있다. 
         - 이런 경우 자동 기능을 적극 사용하는 것이 좋다. 보통 문제가 발생해도 어떤 곳에서 문제가 발생했는지 명확하게 파악하기 쉽다.
     - 기술 지원 로직은 업무 로직과 비교해서 그 수가 매우 적고, 보통 애플리케이션 전반에 걸쳐서 광범위하게 영향을 미친다. 
       - 그리고 업무 로직은 문제가 발생했을 때 어디가 문제인지 명확하게 잘 드러나지만, 기술 지원 로직은 적용이 잘 되고 있는지 아닌지 조차 파악하기 어려운 경우가 많다. 
       - 그래서 이런 기술 지원 로직들은 가급적 수동 빈 등록을 사용해서 명확하게 드러내는 것이 좋다.
       - 애플리케이션에 광범위하게 영향을 미치는 기술 지원 객체는 수동 빈으로 등록해서 딱! 설정 정보에 바로 나타나게 하는 것이 유지보수 하기 좋다.
     
- 비즈니스 로직 중에서 다형성을 적극 활용할 때
  - 의존관계 자동 주입 - 조회한 빈이 모두 필요할 때, List, Map을 다시 보자.
    - DiscountService 가 의존관계 자동 주입으로 Map<String, DiscountPolicy> 에 주입을 받는 상황을 생각해보자. 
      - 여기에 어떤 빈들이 주입될 지, 각 빈들의 이름은 무엇일지 코드만 보고 한번에 쉽게 파악할 수 있을까? 내가 개발했으니 크게 관계가 없지만, 
      - 만약 이 코드를 다른 개발자가 개발해서 나에게 준 것이라면 어떨까?
        - 자동 등록을 사용하고 있기 때문에 파악하려면 여러 코드를 찾아봐야 한다.
        - 이런 경우 수동 빈으로 등록하거나 또는 자동으로하면 특정 패키지에 같이 묶어두는게 좋다! 핵심은 딱 보고 이해가 되어야 한다!

###### 정리
  - 편리한 자동 기능을 기본으로 사용하자
  - 직접 등록하는 기술 지원 객체는 수동 등록
  - 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자

---

### 9. 빈 생명주기 콜백

#### 빈 생명주기 콜백 기본.

- 초기화 작업과 종료 작업의 진행.
  
- 스프링 빈은 다음의 라이프 사이클을 가진다.
  - 객체 생성 -> 의존관계 주입
- 스프링 빈은 객체를 생성하고, 의존관계 주입이 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료된다.
  - 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 후 호출해야 한다.
  
  - 의존관계 주입이 완료된 시점을 아는 법
    - 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.
    - 스프링은 컨테이너가 종료되기 직전 소멸 콜백을 준다. (종료 작업을 안전하게 진행하도록 해줌.)

- 스프링 빈의 이벤트 라이프사이클
  - 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
    - 초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
    - 소멸전 콜백: 빈이 소멸되기 직전에 호출
  - 참고 )
    - 생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다. 
      - 반면에 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는등 무거운 동작을 수행한다.
      - 따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는 객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이 유지보수 관점에서 좋다. 
      - 물론 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우에는 생성자에서 한번에 다 처리하는게 더 나을 수 있다.
  - 참고 )
    - 싱글톤 빈들은 스프링 컨테이너가 종료될 때 싱글톤 빈들도 함께 종료되기 때문에 스프링 컨테이너가 종료되기 직전에 소멸전 콜백이 일어난다. 
    - 싱글톤 처럼 컨테이너의 시작과 종료까지 생존하는 빈도 있지만, 생명주기가 짧은 빈들도 있는데 이 빈들은 컨테이너와 무관하게 해당 빈이 종료되기 직전에 소멸전 콜백이 일어난다. 

- 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원한다.
  - 인터페이스(InitializingBean, DisposableBean)
  - 설정 정보에 초기화 메서드, 종료 메서드 지정
  - @PostConstruct, @PreDestroy 애노테이션 지원

#### 인터페이스 InitializingBean, DisposableBean
- InitializingBean 은 afterPropertiesSet() 메서드로 초기화를 지원한다.
- DisposableBean 은 destroy() 메서드로 소멸을 지원한다

- 초기화, 소멸 인터페이스 단점
  - 스프링 전용 인터페이스로 lifeCycle 폴더에 속한 코드들은 스프링 전용 인터페이스를 의존한다.
  - 초기화, 소멸 메서드의 이름을 변경할 수 없다.
  - 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
  - 참고 )
    - 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법들이고, 지금은 다음의 더 나은 방법들이 있어서 거의 사용하지 않는다.