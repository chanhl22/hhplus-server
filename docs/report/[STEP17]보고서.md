# Kafka 기초 개념 및 동작 원리 보고서

---

# 1. Kafka의 기본 구성 요소
## 1-1. Producer (데이터 생산자)

Producer는 Kafka에 데이터를 전송하는 클라이언트 애플리케이션입니다.

Producer는 특정 Topic으로 메시지를 발행(publish)하며, 메시지에는 일반적으로 키(key), 값(value), 타임스탬프 등의 정보가 포함됩니다.

Producer는 메시지를 전송할 Partition을 선택하는데, 키 기반의 파티셔닝을 통해 동일 키를 가진 메시지를 같은 Partition에 저장함으로써 순서 보장을 가능하게 합니다.

- **주요 역할:** 애플리케이션에서 생성된 이벤트, 로그, 트랜잭션 데이터 등을 Kafka에 전달
- **메시지 전송 방식:** 비동기 전송 지원, 배치 전송으로 성능 최적화 가능

### 🧩 Producer 코드

```kotlin
@Component
class OrderSpringEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, OrderEvent.Completed>
) : OrderEventPublisher {

    override fun publish(event: OrderEvent.Completed) {
        kafkaTemplate.send("order_completed", event)
    }

}
```

1. KafkaTemplate<>
    - Kafka 토픽으로 메시지를 보낼 때 사용.
2. send()
    - "order_completed"라는 Kafka 토픽에도 이벤트 발행

### 📌 key를 생략하면 어떻게 동작하는가?

send() 메서드는 여러개가 있습니다.

```java
public CompletableFuture<SendResult<K, V>> send(String topic, @Nullable V data) {
    ProducerRecord<K, V> producerRecord = new ProducerRecord(topic, data);
    return this.observeSend(producerRecord);
}

public CompletableFuture<SendResult<K, V>> send(String topic, K key, @Nullable V data) {
    ProducerRecord<K, V> producerRecord = new ProducerRecord(topic, key, data);
    return this.observeSend(producerRecord);
}
```

만약 작성한 코드처럼 key를 전달하지 않으면 null로 간주되어 Kafka 브로커에 전달됩니다.

그러면 key를 사용해야할까요?

### 📌 Kafka에서 key의 역할

Kafka의 key는 주로 다음 두 가지 용도로 사용됩니다.

1. 파티션 결정 (Partitioning)
    - Kafka는 토픽을 여러 파티션으로 나누어 데이터를 분산 저장합니다.
    - key가 있을 경우
        - 해당 key의 해시 값을 기반으로 특정 파티션에 고정됩니다.
    - key가 없을 경우
        - Kafka는 Round-Robin 방식으로 파티션을 선택합니다.
2. 같은 key의 메시지는 같은 파티션으로
    - 예: 주문 ID 또는 사용자 ID를 key로 설정하면, 같은 키 값의 메시지는 항상 같은 파티션으로 전송됨 → 순서를 보장할 수 있음.

### 📌 key 없이 보내면 어떤 문제가 생길 수 있을까?

| 항목 | key 없음 (null) | key 있음 |
| --- | --- | --- |
| 파티션 결정 | 라운드로빈 (랜덤) | 해시 기반으로 고정 |
| 순서 보장 | 여러 파티션에 흩어짐 | 같은 key는 같은 파티션 |
| 소비자 측 처리 로직 | key 기준 처리 어려움 | key 기준 분기 가능 |

### 언제 key를 생략해도 되는가?

- 파티션 간 순서 보장이 필요 없는 경우
- 로드밸런싱을 극대화하고 싶은 경우
- key 기반 메시지 그룹핑이 필요 없는 경우

결론적으로, key를 생략해도 괜찮은 상황이 아니라면 key를 설정하는 게 좋다고 합니다.

---

## 1-2. Consumer (데이터 소비자)

Consumer는 Kafka에서 데이터를 읽어가는 클라이언트 애플리케이션입니다.

Consumer는 하나 이상의 Topic을 구독(subscribe)하여 메시지를 수신하며, 처리 후 Offset을 커밋(commit)하여 자신이 읽은 위치를 관리합니다.

Consumer는 주로 실시간 데이터 처리, 분석, 모니터링 등 다양한 목적에 사용됩니다.

- **주요 역할:** Topic의 메시지를 읽고, 필요한 작업을 수행
- **Offset 관리:** 메시지 재처리 또는 중복 방지를 위한 위치 추적

### 🧩 Consumer 코드

```kotlin
@Component
class PlatformExternalEventListener(
    private val platformSendService: PlatformSendService
) {

    @KafkaListener(topics = ["order_completed"], groupId = "platform-service")
    fun handle(event: OrderEvent.Completed, ack: Acknowledgment) {
        val command = PlatformExternalEventMapper.toCommand(event)
        platformSendService.send(command)
        ack.acknowledge()
    }

}
```

1. @KafkaListener
    - Kafka 컨슈머를 선언합니다.
    1. topics
        - 구독할 Kafka 토픽 이름입니다.
    2. groupId
        - 컨슈머 그룹 ID입니다. 같은 그룹에 속한 컨슈머는 메시지를 나눠서 소비합니다.
        - Spring Kafka가 해당 메서드를 하나의 Kafka Consumer로 등록할 때, 어떤 Consumer Group에 속하게 할지를 지정하는 설정입니다.
2. Acknowledgment
    - 수동 커밋을 위한 객체입니다. 명시적으로 커밋해야 메시지가 처리된 것으로 간주됩니다.
3. acknowledge()
    - 수동 커밋 모드를 사용할 때 커밋을 명시적으로 수행합니다.
    - 이 호출이 있어야 Kafka에 "이 메시지 성공적으로 처리했다"고 알립니다.
    - 없으면 다음번 실행 시 같은 메시지를 다시 소비할 수도 있습니다. (중복 소비 가능성).

### 📌 Consumer Group 설정

#### Consumer Group을 다르게 주면?

- groupId가 다르면, 같은 메시지를 여러 Consumer가 각각 중복해서 소비할 수 있습니다.
- 예: A라는 메시지를 groupId = "service-A"와 "service-B"인 Listener가 각각 받게 됩니다.

#### Consumer Group이 같으면?

- 파티션 단위로 메시지를 나눠서 병렬로 처리합니다.
- 동일한 groupId의 Listener가 2개 이상이면, Kafka가 자동으로 파티션을 분산 배정합니다.

즉, 같은 서비스 내에서 서로 다른 종류의 메시지를 처리하는 Listener가 많다면, groupId를 Listener마다 명시적으로 부여하는 것이 좋습니다.
* 예: 주문 완료, 취소, 결제 등 각각 다른 groupId를 쓰면 장애 시 개별 복구 및 모니터링이 쉬움.

> application.yaml에서 설정한 spring.kafka.consumer.group-id와 같은 역할을 하며, 우선순위는 코드에 작성한 @KafkaListener의 groupId가 더 높습니다.

---

## 1-3. Broker

**Kafka Broker**는 Apache Kafka의 핵심 구성 요소 중 하나로, **Producer로부터 전송된 메시지를 받아 저장하고, Consumer가 해당 메시지를 가져갈 수 있도록 제공하는 중간 서버**입니다.

즉, **Kafka 클러스터에서 데이터를 실제로 저장하고 관리하는 서버**가 바로 **Broker**입니다.

### 🧭 Broker의 주요 역할

| 역할 | 설명 |
| --- | --- |
| ✅ 메시지 저장 | Producer로부터 받은 메시지를 지정된 **Topic의 Partition**에 디스크에 저장합니다. |
| ✅ 메시지 제공 | Consumer가 요청하면 저장된 메시지를 **Offset 기반**으로 읽어 전달합니다. |
| ✅ 파티션 관리 | 각각의 Topic은 여러 Partition으로 나뉘며, 각 Partition은 특정 Broker가 책임지고 관리합니다. |
| ✅ 리더/팔로워 관리 | Partition은 리더와 팔로워 구조로 운영되며, 리더가 메시지를 받아 저장한 후, 팔로워가 이를 복제합니다. |
| ✅ 클러스터 메타데이터 공유 | ZooKeeper 또는 KRaft 모드를 통해 클러스터의 상태와 메타데이터를 공유합니다. |

### 🗂️ 구성과 구조

#### Kafka Cluster와 Broker
  - Kafka는 하나의 클러스터로 운영되며, 클러스터는 여러 대의 Broker로 구성됩니다.
  - 각 Broker는 고유한 **Broker ID**를 가지고 있으며, 클러스터 내에서 **하나 이상의 Partition**을 관리합니다.

    ```
    Kafka Cluster
      ├── Broker 1 (broker.id=1)
      ├── Broker 2 (broker.id=2)
      └── Broker 3 (broker.id=3)
    
    ```
    
#### Topic, Partition, Broker 관계

  - 하나의 **Topic**은 여러 개의 **Partition**으로 나뉘며,
  - 각 Partition은 특정 **Broker**에 할당되어 저장되고 관리됩니다.
  - 파티션마다 **Leader** Broker가 있으며, 나머지 Broker는 **Follower** 역할을 하여 데이터를 복제합니다.

    ```
    Topic: order_events (Partition 3개)
      ├── Partition 0 → Broker 1 (Leader), Broker 2 (Follower)
      ├── Partition 1 → Broker 2 (Leader), Broker 3 (Follower)
      └── Partition 2 → Broker 3 (Leader), Broker 1 (Follower)
    
    ```
### ⚙️ 데이터 저장 및 제공 흐름

#### 메시지 저장 (Producer → Broker)

1. Producer는 메시지를 보낼 Topic과 Key를 지정함
2. Kafka는 Key를 해시하여 **Partition을 결정**
3. 해당 Partition의 Leader Broker가 메시지를 받아 저장 (디스크 기반 Segment에 기록)
4. 설정된 `replication.factor`에 따라 다른 Broker에도 복제됨

#### 메시지 소비 (Broker → Consumer)

1. Consumer는 특정 Topic과 Partition에 대해 Offset을 기준으로 메시지를 요청
2. Partition의 Leader Broker가 해당 메시지를 디스크에서 읽어 Consumer에 전송
3. Consumer는 메시지 수신 후 처리하고, 커밋 여부를 판단 (manual / auto commit)

### 📡 Broker의 확장성과 가용성

| 항목 | 설명 |
| --- | --- |
| 수평 확장성 | Broker는 쉽게 **수평 확장**이 가능하여, 파티션을 분산시켜 **부하 분산 및 병렬 처리** 가능 |
| 장애 대응 | Partition 복제를 통해 Leader Broker 장애 시 Follower가 리더로 승격되어 고가용성 확보 |
| 메시지 유실 방지 | `acks=all`, `min.insync.replicas` 등의 설정을 통해 안전하게 메시지를 저장 가능 |

---

## 1-4. Topic

**Topic**은 Kafka에서 메시지를 **카테고리별로 분류하는 논리적 단위**입니다.

쉽게 말해, Kafka의 **메시지를 주고받는 통로 혹은 채널**이라고 생각할 수 있습니다.

> 예를 들어, 주문 이벤트를 처리하는 경우 order_completed, order_created 같은 Topic을 둘 수 있습니다.

### 🧭 Topic의 주요 역할

| 역할 | 설명 |
| --- | --- |
| ✅ 메시지 분류 | Producer가 전송하는 메시지를 구분해 저장하기 위한 **논리적 구분 단위** |
| ✅ 파티션 단위 분산 처리 | 내부적으로 여러 **Partition**으로 구성되어 있어 **병렬 처리 및 확장성** 제공 |
| ✅ Consumer 그룹을 통한 메시지 소비 | 여러 Consumer가 Topic에 연결되어 메시지를 **독립적이거나 그룹 단위**로 소비 가능 |
| ✅ 메시지 저장소 역할 | 메시지는 Topic의 Partition에 순차적으로 기록되며, 일정 기간 저장됨 |

### 🧱 Topic의 내부 구조

Kafka의 Topic은 실제로는 하나 이상의 **Partition**으로 구성되어 있으며, 각 Partition은 메시지를 **순서대로 저장**하는 큐입니다.

```
Topic: order_completed
  ├── Partition 0: [msg1, msg2, msg3, ...]
  ├── Partition 1: [msg4, msg5, msg6, ...]
  └── Partition 2: [msg7, msg8, msg9, ...]

```

각 Partition은 특정 Kafka **Broker에 분산**되어 저장되며, 병렬로 읽고 쓸 수 있는 단위를 제공합니다.

### 📌 메시지 흐름 (Producer → Topic → Partition → Consumer)

1. **Producer**는 특정 Topic에 메시지를 보냄
2. Kafka는 메시지의 key 또는 라운드로빈 방식 등을 이용해 **Partition**을 결정
3. 해당 Partition의 **Leader Broker**가 메시지를 받아 디스크에 저장
4. **Consumer**는 해당 Topic의 Partition에 접근해 메시지를 읽음 (Offset 기반)

### 🔁 Topic의 순서 보장

- Kafka는 **Partition 단위로 순서를 보장**합니다.
- 즉, 하나의 Partition에 들어간 메시지는 **절대적인 순서가 보장**됩니다.
- 하지만 Topic이 여러 Partition으로 구성될 경우, **전체 Topic 수준의 순서 보장은 불가능**합니다.

> 💡 메시지의 순서를 보장하고 싶다면 하나의 Partition만 사용하는 Topic을 선택하거나, 메시지 key를 지정해 같은 Partition에 들어가도록 설정해야 합니다.

### 👥 하나의 Topic, 여러 Consumer

Kafka의 Topic은 여러 Consumer가 동시에 구독할 수 있습니다.

- **독립 소비**: 서로 다른 Consumer Group이면 **서로 영향을 주지 않고** 데이터를 모두 소비할 수 있음.
- **병렬 소비**: 같은 Consumer Group일 경우, 파티션 단위로 나뉘어 **분산 소비**됨.

```
Topic: order_completed (Partition 3)
Consumer Group A
  ├── Consumer 1 → Partition 0
  ├── Consumer 2 → Partition 1
  └── Consumer 3 → Partition 2
```

---

## 1-5. Partition

Partition(파티션)은 Kafka에서 Topic을 **물리적으로 나누는 단위**입니다.

Kafka Topic은 하나 이상의 Partition으로 구성되며, 각 Partition은 **순서가 보장**됩니다.

> 즉, Topic은 논리적인 이름이고, Partition은 실제 메시지를 저장하고 처리하는 단위입니다.

### 🔨 Partition의 역할과 필요성

Kafka가 대용량 메시지를 빠르게 처리하고 확장성을 갖는 가장 큰 이유는 바로 **Partition 기반 구조** 덕분입니다.

| 역할 | 설명 |
| --- | --- |
| ✅ **병렬 처리** 지원 | Partition이 여러 개면, Producer/Consumer가 병렬로 작업 가능 |
| ✅ **확장성** | 파티션 수를 늘리면 더 많은 노드에 분산 가능 |
| ✅ **순서 보장** | **하나의 파티션 내부에서는 순서가 절대적으로 보장**됨 |
| ✅ **데이터 분산 저장** | 여러 Broker에 파티션을 나누어 저장함으로써 부하 분산 |

### 🧱 Partition 구조

각 Partition은 **Append-only 로그 파일**이며, 다음과 같이 구성됩니다:

```
Partition 0 (Leader Broker 1)
  ├── Offset 0: message1
  ├── Offset 1: message2
  └── Offset 2: message3

Partition 1 (Leader Broker 2)
  ├── Offset 0: message4
  └── Offset 1: message5

```

- 메시지는 **Offset(오프셋)**이라는 순차 ID로 저장
- 파티션마다 **리더(Leader)**가 있고, 나머지는 복제본(Follower)로 동작

### 🧭 메시지 전송: Producer → Partition

#### 메시지를 어떤 파티션에 넣을지 결정하는 방식

| 방법 | 설명 |
| --- | --- |
| 🔑 **Key 기반** | 메시지에 key가 있다면 `hash(key) % partition 수`로 결정 ⇒ **동일 key는 같은 파티션** |
| 🔁 **라운드 로빈 방식** | key가 없으면 순차적으로 파티션을 선택해 분산 |
| 🛠️ **사용자 정의 Partitioner** | 필요 시 커스텀 로직으로 파티션 지정 가능 |

```kotlin
kafkaTemplate.send("order_completed", key, event)
```

> 💡 동일한 key로 보내면 항상 같은 파티션으로 들어가므로, 순서 보장이 가능합니다.

### 👨‍👩‍👧‍👦 Consumer와 Partition의 관계

Consumer는 **Partition 단위로 메시지를 소비**합니다.

한 파티션은 **오직 하나의 Consumer만이 읽을 수 있으며**, 반대로 하나의 Consumer가 여러 파티션을 읽을 수는 있습니다.

#### 예시:

- Partition 3개
- Consumer Group A (2명)

```
Consumer A1 → Partition 0
Consumer A2 → Partition 1
Consumer A1 → Partition 2 (여유 파티션은 한 Consumer가 추가로 소비)
```

> 💡 Consumer 수보다 Partition이 적으면 일부 Consumer는 대기 상태가 됩니다.

### 🔁 순차 처리 보장

- Kafka는 **Partition 내에서는 메시지 순서를 절대적으로 보장**합니다.
- 단, **Partition이 여러 개일 경우 전체 Topic 수준에서의 순서는 보장되지 않습니다.**

> 따라서 순서가 중요한 메시지(예: 주문 상태)는 key를 지정해 같은 Partition에 보내야 합니다.

### ⚡ 병렬 처리 보장

- 여러 파티션에 Producer가 동시에 메시지를 적재 가능
- 여러 Consumer가 각 파티션을 병렬로 읽음
- 이 구조 덕분에 Kafka는 **초당 수백만 건의 메시지 처리량**을 지원할 수 있음

### 🔄 Partition 수 변경

- Topic 생성 시 파티션 수를 지정할 수 있으며, **나중에 증가만 가능**
- 기존 파티션에는 영향이 없으며, **신규 메시지부터 새 파티션에 분산**

> ⚠️ 단, 파티션 수 변경은 Consumer group 처리 방식에 영향을 줄 수 있으므로 신중히 해야 합니다.

### 🧠 Partition 설계 전략

| 전략 | 설명 |
| --- | --- |
| 🔢 적절한 파티션 수 설정 | 병렬성 확보를 위해 Consumer 수 이상으로 설정 |
| 🔐 Key 사용 | 순서가 중요한 메시지는 같은 key를 설정 |
| ⚖️ 데이터 균형 | key 분포가 쏠리면 일부 파티션에만 부하 집중됨 ⇒ key 분산 유도 필요 |
| 🧪 테스트 기반 튜닝 | 처리량/속도/지연을 측정해 최적의 파티션 수 조정 |

---

# 2. Kafka의 메시지 처리 메커니즘

## 순차성과 병렬성의 완벽한 균형

Kafka는 **대규모 데이터 스트리밍 처리 시스템**으로서, **"순서를 지키면서도 빠르게 처리하는"** 두 마리 토끼를 잡기 위한 구조적 메커니즘을 갖추고 있습니다.

그 핵심은 바로 **Partition 기반의 메시지 처리 메커니즘**입니다.


## 2-1. 순차 처리 보장 (Message Ordering Guarantee)

### ✅ 개요

Kafka는 메시지를 **순서대로 처리해야 하는 상황**에서 매우 유용한 기능을 제공합니다. 단, 그 순서는 **Partition 단위로만 보장**됩니다.

> 같은 Partition에 저장된 메시지는 Producer → Broker → Consumer로 순서가 절대적으로 유지됩니다.

### 🛠️ 동작 방식

1. **Producer**가 메시지를 전송할 때 key를 지정하면 해당 key에 대한 hash 값을 기준으로 **고정된 Partition**에 기록됩니다.
2. Kafka는 해당 Partition에 메시지를 **append-only log** 형태로 순차적으로 저장합니다.
3. **Consumer**는 각 Partition의 메시지를 **Offset 순서대로 차례로 소비**합니다.

```
Partition 0: [Offset 0] → [Offset 1] → [Offset 2]
```

### 🎯 순차 처리 사용 사례

- 주문 상태 처리 (ORDER_CREATED → ORDER_PAID → ORDER_COMPLETED)
- 결제 트랜잭션
- 물류 추적 이벤트
- 특정 사용자에 대한 연속 동작 처리

### ⚠️ 주의할 점

- **Topic 전체의 순서 보장은 안 됨.** → Partition 수가 2 이상일 경우, Partition 간의 메시지 순서는 의미가 없음.
- 따라서 동일한 key (예: 주문 ID, 사용자 ID)로 Partition을 고정하여 순서를 유지해야 함.

## 2-2. 병렬 처리 지원 (Parallelism & Scalability)

Kafka는 단일 노드 처리 한계를 넘어서기 위해 **Partition을 기반으로 병렬 처리**를 지원합니다.

### ⚙️ 구조적 병렬성

Kafka는 다음의 구조로 병렬성을 확보합니다:

| 구성 요소 | 병렬 처리 관점에서의 역할 |
| --- | --- |
| **Partition** | 하나의 Topic을 여러 분산 단위로 쪼갬 (N개) |
| **Broker** | Partition들을 여러 서버에 분산 저장 |
| **Consumer Group** | 여러 Consumer가 Partition을 나눠 읽음 |

### 🚀 처리 흐름 예시

- Topic `event-log`가 4개의 Partition을 가짐
- Consumer Group `log-group`에 Consumer가 4명 존재

```
Partition 0 → Consumer A
Partition 1 → Consumer B
Partition 2 → Consumer C
Partition 3 → Consumer D
```

모든 Consumer가 **동시에 메시지를 읽고 처리**하므로, 병렬 처리 성능이 극대화됩니다.

### 📈 병렬 처리의 장점

- 처리량(Throughput) 극대화
- 메시지 지연 최소화
- 시스템 확장성 확보
- 대용량 트래픽 안정적 처리

### 🧩 병렬 처리에 대한 고려사항

| 고려 항목 | 설명 |
| --- | --- |
| ⚖️ Partition 수 | 병렬 처리 수준은 **Partition 수에 의존** (Partition 수 ≥ Consumer 수 권장) |
| 💬 Key 분산 | 특정 Key 쏠림 현상이 있을 경우 일부 Partition에 부하 집중 가능 |
| 🔁 리밸런싱 | Consumer 수가 변경되면 Partition 재할당이 발생함 (일시적으로 처리 중단 가능) |

## ✅ 정리: Kafka 메시지 처리의 두 축

| 항목 | 순차 처리 보장 | 병렬 처리 지원 |
| --- | --- | --- |
| 기준 단위 | Partition 내부 | Partition 간 |
| 핵심 기술 | Offset 기반 append log | Partition 분산 및 Consumer Group |
| 활용 조건 | Key 고정 → 같은 Partition | Partition 수 ≥ Consumer 수 |
| 보장 수준 | 메시지 순서 완벽 보장 | 고성능 처리 보장 |