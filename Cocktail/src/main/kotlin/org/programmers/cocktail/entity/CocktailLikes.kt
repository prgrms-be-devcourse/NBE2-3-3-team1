package org.programmers.cocktail.entity

import jakarta.persistence.*
import org.programmers.cocktail.entity.Users
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktails_likes")
@Table(
    name = "cocktail_likes",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "cocktail_id"])]
)
class CocktailLikes(
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
    var createdAt: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
        protected set

    // JPA 기본 생성자
    constructor() : this(
        users = Users(), // 기본값
        cocktails = Cocktails() // 기본값
    )
}
