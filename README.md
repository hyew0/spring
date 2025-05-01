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

## Test 부분 개념

- @DisplayName : junit에서 한글로 이름을 작성 가능하게 함.