package kr.hhplus.be.server.domain.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User(
    val name: String,
    val pointId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
        }
    }

}
