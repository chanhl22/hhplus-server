package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.fixture.stock.StockDomainFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class StockRepositoryImplTest {

    @Autowired
    private lateinit var stockRepositoryImpl: StockRepositoryImpl

    @Autowired
    private lateinit var stockJpaRepository: StockJpaRepository

    @DisplayName("상품 id로 재고를 조회한다.")
    @Test
    fun findByProductId() {
        //given
        val stock = StockDomainFixture.create(stockId = 0L, productId = 1)
        val savedStock = stockJpaRepository.save(stock)

        //when
        val result = stockRepositoryImpl.findByProductId(1)

        //then
        assertThat(result.quantity).isEqualTo(savedStock.quantity)
    }

    @DisplayName("상품 id로 재고 리스트를 조회한다.")
    @Test
    fun findByProductIdIn() {
        //given
        val stock1 = StockDomainFixture.create(stockId = 0L, productId = 1)
        val stock2 = StockDomainFixture.create(stockId = 0L, productId = 2)
        stockJpaRepository.save(stock1)
        stockJpaRepository.save(stock2)

        //when
        val result = stockRepositoryImpl.findByProductIdIn(listOf(1, 2))

        //then
        assertThat(result).hasSize(2)
    }

    @DisplayName("재고를 저장한다.")
    @Test
    fun saveAll() {
        //given
        val stock1 = StockDomainFixture.create(stockId = 0L, productId = 1)
        val stock2 = StockDomainFixture.create(stockId = 0L, productId = 2)

        //when
        stockRepositoryImpl.saveAll(listOf(stock1, stock2))

        //then
        val findStock1 = stockJpaRepository.save(stock1)
        assertThat(findStock1.quantity).isEqualTo(10000)
        val findStock2 = stockJpaRepository.save(stock2)
        assertThat(findStock2.quantity).isEqualTo(10000)
    }

}