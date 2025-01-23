package org.programmers.cocktail.repository.users

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.programmers.cocktail.entity.Users
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class UsersRepositoryImpl : UsersRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    override fun searchByKeyword(keyword: String, pageable: Pageable): Page<Users> {
        val jpql =
            "select u from users u where lower(u.name) like :keyword or lower(u.email) like :keyword"

        val results = entityManager!!.createQuery(jpql, Users::class.java)
            .setParameter("keyword", "%" + keyword.lowercase(Locale.getDefault()) + "%")
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList

        val countJpql =
            "select count(u) from users u where lower(u.name) like :keyword or lower(u.email) like :keyword"
        val total = entityManager.createQuery(countJpql, Long::class.java)
            .setParameter("keyword", "%" + keyword.lowercase(Locale.getDefault()) + "%")
            .singleResult
        return PageImpl(results, pageable, total)
    }
}