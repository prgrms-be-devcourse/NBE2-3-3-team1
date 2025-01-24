package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "authorities")
@Table(name = "authorities")
class Authorities(
    @Column(nullable = false, length = 50)
    var role: String, // null 불허

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val users: Users // null 불허
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
        protected set

    // JPA 기본 생성자
    constructor() : this(
        role = "",
        users = Users() // 기본 사용자 객체 (혹은 null 가능성 관리 필요)
    )
}
