package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktails_likes")
@Table(
    name = "cocktail_likes",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "cocktail_id"])]
)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
class CocktailLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private val users: Users? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cocktail_id", nullable = false)
    @ToString.Exclude
    private val cocktails: Cocktails? = null

    @CreationTimestamp
    @Column(updatable = false)
    private var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    private val updatedAt: LocalDateTime? = null
}
