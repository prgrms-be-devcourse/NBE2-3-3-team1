package org.programmers.cocktail.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "users")
class Users(
    @Column(nullable = false, length = 30, unique = true)
    var email: String, // 불변

    @Column(nullable = false, length = 20)
    var name: String, // 수정 가능

    @Column(nullable = false)
    var password: String, // 수정 가능

    @Column(nullable = false)
    var gender: String, // 수정 가능

    @Column(nullable = false)
    var age: Int // 수정 가능
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null // 불변

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    var authorities: MutableList<Authorities> = ArrayList()
        protected set

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comments> = ArrayList()
        protected set

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cocktailLists: MutableList<CocktailLists> = ArrayList()
        protected set

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cocktailLikes: MutableList<CocktailLikes> = ArrayList()
        protected set

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
        protected set

    // JPA에서 필요한 기본 생성자 추가
    constructor() : this(
        email = "",
        name = "",
        password = "",
        gender = "",
        age = 0
    )
}
