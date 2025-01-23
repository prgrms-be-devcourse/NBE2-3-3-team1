package org.programmers.cocktail.search.service;

import org.modelmapper.ModelMapper;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.search.dto.UsersTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersMapper {

    @Autowired
    private ModelMapper modelMapper;

    //entity->TO변환
    public UsersTO convertToUsersTO(Users users) {
        if (users == null) {
            return null;
        }
        return modelMapper.map(users, UsersTO.class);
    }
}
