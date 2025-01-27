package org.programmers.cocktail.admin.service

import org.programmers.cocktail.exception.ErrorCode
import org.programmers.cocktail.global.exception.BadRequestException
import org.programmers.cocktail.repository.users.UsersRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class AdminAuthService(
    val passwordEncoder: BCryptPasswordEncoder,
    private val usersRepository: UsersRepository
) {
    fun authenticate(email: String, password: String?) {
        val user = usersRepository.findByEmail(email)
            ?.orElseThrow({ BadRequestException(ErrorCode.USER_NOT_FOUND) })!!

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadRequestException(ErrorCode.INVALID_PASSWORD)
        }
    }
}
