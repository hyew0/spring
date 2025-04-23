# 스프링 입문

## 내용 정리

+ postmapping

### AOP

#### AOP가 필요한 상황
- 모든 메소드의 호출 시간을 측정하고 싶다면?
- 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)
- 회원 가입 시간, 회원 조회 시간을 측정하고 싶다면?


- aop는 @Aspect를 적어줘야 해당 파일을 aop로 쓸 수 있음
    + aop는 컴포넌트 스캔(@Component) 이용해서 등록해도 되고 bean으로 등록해도 된다.
    
  ```java
    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
    ```
  
#### 문제
- 회원가입, 회원 조회에 시간을 측정하는 기능은 핵심 관심 사항이 아니다.
- 시간을 측정하는 로직은 공통 관심 사항이다.
- 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다.
- 시간을 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다.
- 시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.

#### AOP 적용
- AOP: Aspect Oriented Programming
  + 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern) 분리

##### 스프링 AOP 어노테이션 정리

- 스프링에서 AOP(Aspect Oriented Programming, 관점 지향 프로그래밍)는 여러 메소드에 공통 기능을 분리하여 적용할 수 있게 해준다. 
  - 대표적으로 로깅, 보안, 트랜잭션 처리와 같은 기능이 있다.
- 아래는 스프링 AOP에서 사용되는 주요 어노테이션과 그 설명이다.

---

##### @Before("패턴")

- 지정한 패턴에 해당하는 메소드가 **실행되기 전에** 동작한다.
- 주로 로그 출력, 권한 체크 등의 기능을 처리할 때 사용한다.
- 해당 메소드의 반환 타입은 `void`여야 한다.

```java
@Before("execution(* com.example.service.*.*(..))")
public void logBefore() {
    System.out.println("메소드 실행 전입니다.");
}
```

##### @After("패턴")
- 지정한 패턴에 해당하는 메소드가 실행된 후에 동작한다.
- 주로 작업 종료 후 리소스 정리나 로그 출력 등에 사용한다.
- 해당 메소드의 반환 타입은 void여야 한다.

```java
@After("execution(* com.example.service.*.*(..))")
public void logAfter() {
    System.out.println("메소드 실행 후입니다.");
}
```

##### @Around("패턴")
- 지정한 패턴에 해당하는 메소드의 실행 전과 후 모두에서 동작한다.
- 메소드 실행 전후에 특정 처리를 할 수 있으며, 메소드 자체의 실행도 직접 제어할 수 있다.
- 반환 타입은 Object이어야 하며, 원래 메소드를 실행하고 그 결과를 반환해야 한다.

```java
@Around("execution(* com.example.service.*.*(..))")
public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("메소드 실행 전입니다.");
    Object result = joinPoint.proceed(); // 원래 메소드 실행
    System.out.println("메소드 실행 후입니다.");
    return result; // 결과 반환
}
```

#### Pointcut 표현식이란?
- AOP 어노테이션 내부에 "execution(...)" 같은 형식으로 메소드 패턴을 지정하는 문자열을 Pointcut 표현식이라고 한다.
- 이를 통해 어떤 메소드에 AOP 기능을 적용할지 정할 수 있다.


```java

execution(* com.example.service.*.*(..))
```
 - *: 반환 타입 무관
- com.example.service.*: 해당 패키지 내의 모든 클래스
- .*(..): 모든 메소드, 파라미터 수나 타입도 무관

- 이와 같은 AOP 어노테이션을 통해 코드의 핵심 로직과 부가 기능을 분리하여 더욱 깔끔하고 유지보수가 쉬운 코드를 작성할 수 있다.