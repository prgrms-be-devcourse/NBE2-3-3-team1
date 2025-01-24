package org.programmers.cocktail.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "comments")
@Table(name = "comments")
class Comments(
    @Column(nullable = false)
    var content: String, // 변경 가능성 있는 필드는 var로 선언

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cocktail_id", nullable = false)
    var cocktails: Cocktails, // 불변성 유지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var users: Users // 불변성 유지
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null // 불변성 유지

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
        protected set

    // JPA 기본 생성자
    constructor() : this(
        content = "",
        cocktails = Cocktails(), // 기본값
        users = Users() // 기본값
    )
}
