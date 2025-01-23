package org.programmers.cocktail.search.service

import org.modelmapper.ModelMapper
import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.search.dto.UsersTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersMapper {
    @Autowired
    private val modelMapper: ModelMapper? = null

    //entity->TO변환
    fun convertToUsersTO(users: Users?): UsersTO? {
        if (users == null) {
            return null
        }
        return modelMapper!!.map(users, UsersTO::class.java)
    }
}
