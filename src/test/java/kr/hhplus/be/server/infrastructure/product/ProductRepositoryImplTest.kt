package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.fixture.product.ProductDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class ProductRepositoryImplTest {

    @Autowired
    private lateinit var productRepositoryImpl: ProductRepositoryImpl

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @DisplayName("상품을 조회한다.")
    @Test
    fun find() {
        //given
        val product = ProductDomainFixture.create(productId = 0L)
        val savedProduct = productJpaRepository.save(product)

        //when
        val result = productRepositoryImpl.find(savedProduct.id)

        //then
        assertThat(result)
            .extracting("name", "price", "description")
            .containsExactly(
                "무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰."
            )
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    fun findAllByIdIn() {
        //given
        val product1 = ProductDomainFixture.create(productId = 0L)
        val product2 = ProductDomainFixture.create(productId = 0L)
        val product3 = ProductDomainFixture.create(productId = 0L)
        val savedProduct1 = productJpaRepository.save(product1)
        val savedProduct2 = productJpaRepository.save(product2)
        val savedProduct3 = productJpaRepository.save(product3)

        //when
        val result = productRepositoryImpl.findAllByIdIn(listOf(savedProduct1.id, savedProduct2.id, savedProduct3.id))

        //then
        assertThat(result).hasSize(3)
            .extracting("name", "price", "description")
            .containsExactly(
                Tuple.tuple("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰."),
                Tuple.tuple("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰."),
                Tuple.tuple("무선 블루투스 이어폰", 129000, "고음질 무선 블루투스 이어폰.")
            )
    }

}