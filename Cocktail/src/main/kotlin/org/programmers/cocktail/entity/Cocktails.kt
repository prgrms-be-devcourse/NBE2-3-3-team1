package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktails")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
class Cocktails @Builder constructor(
    @field:Column(nullable = false) private var name: String,
    @field:Column(
        nullable = false,
        length = 600
    ) private var ingredients: String,
    @field:Column(
        nullable = false,
        length = 600
    ) private var recipes: String,
    @field:Column(nullable = true) private var category: String,
    @field:Column(
        nullable = true
    ) private var alcoholic: String,
    @field:Column(nullable = true) private var image_url: String, // 초기 default값은 0
    @field:Column(nullable = false) private var hits: Long, // 초기 default값은 0
    @field:Column(nullable = false) private var likes: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @OneToMany(mappedBy = "cocktails", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments: List<Comments> = ArrayList()

    @OneToMany(mappedBy = "cocktails", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val cocktailLists: List<CocktailLists> = ArrayList()

    @CreationTimestamp
    @Column(updatable = false)
    private var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    private val updatedAt: LocalDateTime? = null
}
