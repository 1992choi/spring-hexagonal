# Spring-Hexagonal
## 프로젝트 정보
- 강의
  - 인프런 > 토비의 클린 스프링 - 도메인 모델 패턴과 헥사고날 아키텍처 Part 1
- git repo
  - https://github.com/tobyspringboot/splearn

<hr>

## 프로젝트 아키텍처
### 아키텍처
- 헥사고날 아키텍처이며, 동시에 도메인 모델 패턴을 가짐.
  - 헥사고날 아키텍처
    - 주요 개념
      - 2005년 앨리스터 코번이 제안한 아키텍처
      - 계층형 아키텍처의 단방형 비대칭 구조가 아닌 대칭형 아키텍처
      - 위 아래, 좌 우가 아닌 애플리케이션의 내부와 외부 세계라는 대칭 구조를 가진다.
    - 특징
      - 애플리케이션과 상호작용하는 액터가 바뀌더라도 다시 빌드하지 않고 테스트할 수 있다.
      - UI 디테일이나 기술 정보가 도메인 로직 안으로 노출되지 않도록 보호한다.
      - 컴포넌트를 각각 개발하고 연결하는 방식으로 큰 시스템을 분리할 수 있다.
      - 시간이 지나면서 외부 연결을 다른 것으로 변경할 수 있다.
      - 기술 요소를 제거했기 때문에 도메인 설계에 집중할 수 있다.
    - 용어
      - 헥사곤의 내부
        - 쉽게 변하지 않는, 중요한 도메인 로직을 담은 코어 애플리케이션
          - 도메인 로직을 가진 트랜잭션 스크립트
          - 애플리케이션 서비스와 도메인 모델 패턴을 따라서 만든 도메인
      - 헥사곤의 외부
        - 헥사곤과 상호작용하는 모든 것 액터(Actor)
          - 사용자, 브라우저, CLI 명령, 기계, 다른 시스템
          - 운영 환경, DB, 메시징 시스템, 메일 시스템, 원격 서비스
          - 테스트
      - 포트
        - 애플리케이션이 외부 세계와 의도를 가지고 상호작용 하는 아이디어를 캡처한 것.
        - 단순히 데이터를 주고받는 것이 아니라, 명확한 목적과 방향을 가지고 외부와 연결된다.
        - 인터페이스로 만들어진다.
          - 이 때 인터페이스는 롤리팝과 소켓 두 종류로 나뉜다.
            - Lollipop (= Provided Interface)
              - 기능 제공하는 인터페이스
            - Socket (= Required Interface)
              - 기능 요구 잉ㄴ터페이스
      - 어댑터
        - 애플리케이션의 포트를 액터가 직접 연결할 수 없다면 인터페이스의 변환을 위한 어댑터를 도입
          - 기본 흐름도
            - Actor - Port - Hexagon(= Application 이라는 용어로도 사용된다.)
          - Adapter가 포함된 형태
            - Actor - (Adapter) - Port - Hexagon
  - 도메인 모델 패턴

### 계층
- 구분
  - Domain Layer
  - Application Layer
  - Adapter Layer
- 제약사항
  - 의존성은 한 방향으로만 흐른다.
    - 외부(Actor) -> 어댑터 -> 애플리케이션 -> 도메인

<hr>

## 새로 알게된 내용 정리
- @NaturalId / @NaturalIdCache
  - @NaturalId
    - 엔티티에서 비즈니스적으로 유일한 키를 나타냄
    - (ex. 주민번호, 이메일 등)
  - @NaturalIdCache
    - 이 자연 키 기반 조회에 대해 2차 캐시를 활성화함
      - Hibernate에서는 일반적으로 PK(primary key)로만 캐시를 지원하지만, @NaturalIdCache를 추가하면 그 조회 결과를 캐싱할 수 있다.
- ReflectionTestUtils
  - private 필드는 외부에서 직접 접근이 불가능하지만, 테스트 과정에서 부득이하게 값을 주입해야 하는 경우 ReflectionTestUtils를 사용하여 이를 설정할 수 있다.
    - ex. ReflectionTestUtils.setField(member, "id", 1L);
- Mockito를 사용한 테스트코드 작성
  - Mockito를 활용하면 번거롭게 stub을 만들지 않고 테스트가 가능하여, `편의성`과 `테스트코드의 가독성`을 높일 수 있다.
  - 커밋 히스토리 참조
    - 섹션 4. 회원 애플리케이션 서비스 테스트 (1)
- memberRepository.save(member)를 명시적으로 사용하는 이유
  - 단순히 `return member;`만 해도 메서드 레벨에서는 문제 없어 보일 수 있으나,   
    Spring Data JPA의 save()는 실제로 persist 또는 merge 동작을 트리거하며, 이때 Entity lifecycle 이벤트 또는 Domain Event(@DomainEvents) 처리가 함께 이루어질 수 있다.
  - 특히 Event 발행 등의 부수 효과를 기대하는 경우에는 save()를 명시적으로 호출하는 습관을 들이는 것이 바람직하다.
  - Spring Data Repository는 JpaRepository를 포함하고 있으며, save()는 그 핵심 메서드 중 하나로, 저장 외에 이벤트 트리거의 역할도 한다.
  - 커밋 히스토리 참조
    - 섹션 4. 회원 애플리케이션 서비스 테스트 (1)