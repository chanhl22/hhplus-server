package kr.hhplus.be.server

import jakarta.annotation.PreDestroy
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.util.*

@Configuration
class TestcontainersConfiguration {
    @PreDestroy
    fun preDestroy() {
        if (mySqlContainer.isRunning) mySqlContainer.stop()
        if (redisContainer.isRunning) redisContainer.stop()
    }

    companion object {
        val mySqlContainer: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("hhplus")
            .withUsername("test")
            .withPassword("test")
            .apply {
                start()
            }

        val redisContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.2"))
            .withExposedPorts(6379)
            .apply {
                start()
            }

        private val kafkaContainer: KafkaContainer = KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0"),
        ).apply {
            portBindings = listOf("9092:9092")
            start()
        }

        val bootstrapServers: String
            get() = kafkaContainer.bootstrapServers

        init {
            System.setProperty("spring.datasource.url", mySqlContainer.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC")
            System.setProperty("spring.datasource.username", mySqlContainer.username)
            System.setProperty("spring.datasource.password", mySqlContainer.password)

            System.setProperty("spring.data.redis.host", redisContainer.host)
            System.setProperty("spring.data.redis.port", redisContainer.firstMappedPort.toString())

            System.setProperty("spring.kafka.bootstrap-servers", bootstrapServers)
        }
    }

    fun getAdminClient(): AdminClient {
        val props = Properties()
        props[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        return AdminClient.create(props)
    }
}
