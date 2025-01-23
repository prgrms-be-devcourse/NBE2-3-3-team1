package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
class Users @Builder constructor(
    @field:Column(
        nullable = false,
        length = 30,
        unique = true
    ) private var email: String,
    @field:Column(
        nullable = false,
        length = 20
    ) private var name: String,
    @field:Column(nullable = false) private var password: String,
    @field:Column(
        nullable = false
    ) private var gender: String,
    @field:Column(nullable = false) private var age: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val authorities: MutableList<Authorities> = ArrayList()

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val comments: List<Comments> = ArrayList()

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val cocktailLists: List<CocktailLists> = ArrayList()

    @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val cocktailLikes: List<CocktailLikes> = ArrayList()

    fun updateAuthorities(authorities: Authorities) {
        this.authorities.add(authorities)
    }

    @CreationTimestamp
    @Column(updatable = false)
    private var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    private val updatedAt: LocalDateTime? = null
}
