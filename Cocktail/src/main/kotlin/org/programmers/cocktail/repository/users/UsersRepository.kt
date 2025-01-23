package org.programmers.cocktail.repository.users

import jakarta.transaction.Transactional
import org.programmers.cocktail.entity.Users
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.lang.NonNull
import java.time.LocalDateTime
import java.util.*

interface UsersRepository : JpaRepository<Users?, Long?>, UsersRepositoryCustom {
    @NonNull
    override fun findById(id: Long): Optional<Users?>

    fun findAllByAuthorities_Role(role: String?, pageable: Pageable?): Page<Users?>?

    // override fun deleteById(id: Long): Boolean


    fun findByEmail(@NonNull email: String?): Optional<Users?>?

    @Query(value = "select u from users u where u.email = :email")
    fun findsByEmail(email: String?): Users?

    @Query(value = "select u from users u where u.email = :email and u.password = :password")
    fun findByEmailAndPassword(email: String?, password: String?): Users?

    @Modifying
    @Transactional
    @Query(value = "update users u set u.name = :name, u.password = :password where u.id = :id")
    fun updateById(name: String?, password: String?, id: Long?): Int

    fun existsByEmailAndPassword(email: String?, password: String?): Boolean

    @Query("SELECT u FROM users u WHERE u.updatedAt > :lastSyncTime")
    fun findUpdatedSince(@Param("lastSyncTime") lastSyncTime: LocalDateTime?): List<Users?>?
}