package org.programmers.cocktail.login.service

import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.login.dto.UserRegisterDto
import org.programmers.cocktail.repository.users.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LoginService(@field:Autowired private val usersRepository: UsersRepository) :
    UserDetailsService {
    fun insert(to: UserRegisterDto): Int {
        val flag = 0

        val users = to.email?.let { to.name?.let { it1 ->
            to.password?.let { it2 ->
                to.gender?.let { it3 ->
                    to.age?.let { it4 ->
                        Users(it,
                            it1, it2, it3, it4
                        )
                    }
                }
            }
        } }

        if (users != null) {
            usersRepository.save(users)
        }

        return flag
    }

    fun findByEmail(email: String?): Users? {
        val users = usersRepository.findsByEmail(email)

        return users
    }


    fun updateUser(name: String?, password: String?, id: Long?): Int {
        val flag = usersRepository.updateById(name, password, id)

        return flag
    }


    fun deleteUser(id: Long): Int {
        println("deleteUser(Long id): $id)")

        usersRepository.deleteById(id)

        return if (usersRepository.existsById(id)) {
            0
        } else {
            1
        }
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        return null
    }
}