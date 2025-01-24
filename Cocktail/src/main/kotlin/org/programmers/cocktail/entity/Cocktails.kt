package org.programmers.cocktail.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktails")
@Table(name = "cocktails")
class Cocktails(
    @Column(nullable = false)
    var name: String, // 필수 필드, null 불허

    @Column(nullable = false)
    var hits: Long = 0, // 기본값 0

    @Column(nullable = false)
    var likes: Long = 0 // 기본값 0
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null // 자동 생성되는 ID 필드

    @OneToMany(mappedBy = "cocktails", cascade = [CascadeType.ALL], orphanRemoval = true)
    val mutableComments: MutableList<Comments> = mutableListOf()
    val comments: List<Comments> get() = mutableComments.toList() // 읽기 전용 컬렉션

    @OneToMany(mappedBy = "cocktails", cascade = [CascadeType.ALL], orphanRemoval = true)
    val mutableCocktailLists: MutableList<CocktailLists> = mutableListOf()
    val cocktailLists: List<CocktailLists> get() = mutableCocktailLists.toList() // 읽기 전용 컬렉션

    @Column(nullable = true)
    var image_url: String? = null
        protected set // 외부에서 수정 불가, 클래스 내부에서만 변경 가능

    @Column(nullable = false, length = 600)
    var ingredients: String = ""
        protected set // 기본값 빈 문자열

    @Column(nullable = false, length = 600)
    var recipes: String = ""
        protected set // 기본값 빈 문자열

    @Column(nullable = true)
    var category: String? = null
        protected set

    @Column(nullable = true)
    var alcoholic: String? = null
        protected set

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        protected set

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
        protected set

    // JPA 기본 생성자
    constructor() : this(
        name = "",
        hits = 0,
        likes = 0
    )
}
