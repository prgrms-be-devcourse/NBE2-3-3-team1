package org.programmers.cocktail.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "total_hits_log")
class TotalHitsLog(
    @Column(nullable = false)
    var totalHits: Long, // 변경 가능성을 고려해 var로 선언

    @Column(nullable = false, unique = true)
    val recordedAt: LocalDateTime // 불변성을 유지하기 위해 val로 선언
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null // 불변성 유지

    // JPA 기본 생성자
    constructor() : this(
        totalHits = 0L, // 기본값 설정
        recordedAt = LocalDateTime.now() // 기본값 설정
    )
}
