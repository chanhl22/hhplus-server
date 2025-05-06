# 보고서: 인기상품 조회 성능 개선을 위한 Redis 캐싱 전략 적용

## 1️⃣ 배경: 증가하는 트래픽과 반복되는 DB 조회

인기상품 조회는 거의 모든 사용자가 진입 시 호출됩니다.  
인기상품은 일정 주기로 갱신되며 자주 변경되지 않지만 호출 빈도는 매우 높습니다.  
동일한 요청에 대해 **반복적인 DB 조회** 발생하고 결국은 **불필요한 리소스 낭비**로 이어집니다.

| 항목 | 설명 |
| --- | --- |
| 📋 호출 구조 | API 호출 → DB 조회 → 정렬 후 반환 |
| 📦 데이터 특성 | 주기적 배치로 갱신되는 정적 데이터 |
| 🔄 요청 빈도 | 사용자 진입 시마다 반복 호출 |
| ⚠️ 병목 지점 | 매 요청마다 DB로부터 계산된 데이터 조회 |

---

## 2️⃣ 문제 정의: 인기상품 API의 병목 지점

인기상품은 "최근 3일간 판매량 상위 5개 상품"을 반환하는 API로 현재 구조는 아래와 같습니다.

```
Client ──▶ API Server ──▶ DB (인기상품 필터링 + 조회) ──▶ 애플레케이션 계산 ──▶ 응답
```

하지만 해당 API는 다음과 같은 문제점이 있었습니다.
- 인기상품 API 응답 지연 (최대 250ms 이상)
- 데이터는 자주 변경되지 않지만 호출 빈도가 높음
- DB CPU 사용률 상승
- 스파이크 트래픽 발생 시 API 장애 위험

---

## 3️⃣ 해결 방안: Redis 캐시 도입을 통한 성능 개선

### ✅ 적용 대상
- 인기상품 조회 API: /api/products/best
- 조회 조건: 최근 3일 기준 판매량 상위 5개 상품

### 🧠 캐싱 전략
- Redis를 사용한 캐싱 도입

| 항목 | 전략 |
| --- | --- |
| 🔑 캐시 키 | findTopSellingProducts |
| ⏱ TTL (유효기간) | 1일 또는 배치 완료 시 갱신 |
| 📚 캐시 방식 | Look Aside 패턴 |
| 🧵 갱신 트리거 | 배치 후 갱신 or TTL 만료 |

### 💡 Cache Aside 패턴 적용
  - 캐시에 데이터가 없을 경우 → DB 조회 후 캐시에 저장
  - 캐시에 데이터가 있을 경우 → Redis에서 바로 응답

---

## 4️⃣ 구현 상세 및 테스트
### 🔧 Spring Boot + Redis 캐시 설정
- 인기상품 서비스에 `@Cacheable` 활용
    ```kotlin
    @Cacheable(cacheNames = ["findTopSellingProducts"])
    fun findTopSellingProducts(): List<ProductInfo.FindTopSales> {
        // DB 조회 및 가공 로직
    }
    ```

- 배치 후 갱신용 `@CacheEvict` 추가
    ```kotlin
    @CacheEvict(cacheNames = ["findTopSellingProducts"], allEntries = true)
    fun clearProductStatisticsCache() {
    }
    ```

- 현 API에 특성을 고려한 적절한 TTL 설정
    ```kotlin
    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(redisObjectMapper())
                )
            )
            .entryTtl(Duration.ofDays(1))
    }
    ```

### 📊 [비교 데이터] Redis 적용 전/후 응답 속도 비교

- 적용 전
![img.png](image/step12/img.png)

- 적용 후
![img_1.png](image/step12/img_1.png)

### 📉 성능 개선 확인
| 테스트 조건 | Redis 미적용 | Redis 적용 |
| --- | --- | --- |
| 평균 응답 속도 | **89ms** | **48ms** |
| 최대 응답 시간 | 249ms | 80ms |

**결과적으로 Redis 적용을 통해 평균 응답 속도는 약 46% 감소했으며, 특히 최대 응답 시간은 약 68%나 크게 감소했습니다.**

---

## 5️⃣ 한계점 및 향후 고려사항

- **데이터 정합성 문제**
    - 실시간 데이터 반영 어려움
    - 현재는 일정 시간 후 캐시를 삭제하며 갱신을 하고 있지만 TTL과 캐시 삭제 사이 지연 가능성
    - 보완: 중요 시점마다 수동 캐시 삭제
- **캐시 고갈 시 성능 하락 가능성**
    - Redis 장애 대비 fallback 로직 필요
- **Redis 과부하 방지**
    - Key 수, TTL 전략, evict 정책 고려 필요

---

## 6️⃣ 결론

- Redis 캐싱 도입으로 인기상품 조회 API의 **평균 응답 속도는 약 46% 감소했으며, 특히 최대 응답 시간은 약 68% 감소**
- 적절한 TTL 및 갱신 전략을 병행하면 **데이터 정합성과 성능을 모두 확보 가능**

### 🎯 **추천 전략**
- 조회가 많고 변경이 적은 데이터에 Redis 캐싱 적용
- Look Aside 패턴을 기본으로, 명시적 갱신 로직 병행
- 캐시 상태 모니터링 + 장애 대비 fallback 로직 구성