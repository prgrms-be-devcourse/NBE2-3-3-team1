package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.Getter
import java.time.LocalDateTime

@Entity
@Getter
class TotalHitsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(nullable = false)
    private var totalHits: Long? = null

    @Column(nullable = false, unique = true)
    private var recordedAt: LocalDateTime? = null

    constructor(totalHits: Long?, recordedAt: LocalDateTime?) {
        this.totalHits = totalHits
        this.recordedAt = recordedAt
    }

    protected constructor()
}
