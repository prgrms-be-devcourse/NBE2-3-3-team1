package org.programmers.cocktail.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktail_lists")
@Table(
    name = "cocktail_lists",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "cocktail_id"])]
)
class CocktailLists(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var users: Users, // 불변성 유지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cocktail_id", nullable = false)
    var cocktails: Cocktails // 불변성 유지
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null // 불변성 유지

    @CreationTimestamp
    @Column(updatable = false)
    protected var createdAt: LocalDateTime? = null // 내부에서만 수정 가능

    @UpdateTimestamp
    protected var updatedAt: LocalDateTime? = null // 내부에서만 수정 가능

    // JPA 기본 생성자
    constructor() : this(
        users = Users(), // 기본값
        cocktails = Cocktails() // 기본값
    )
}
