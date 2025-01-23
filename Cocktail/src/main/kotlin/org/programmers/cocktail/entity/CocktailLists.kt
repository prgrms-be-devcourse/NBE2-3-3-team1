package org.programmers.cocktail.entity

import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "cocktail_lists")
@Table(
    name = "cocktail_lists",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "cocktail_id"])]
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class CocktailLists {
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
