package com.gradingapp.service.mapper;

import com.gradingapp.domain.*;
import com.gradingapp.service.dto.ActiveUsersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActiveUsers and its DTO ActiveUsersDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActiveUsersMapper extends EntityMapper<ActiveUsersDTO, ActiveUsers> {



    default ActiveUsers fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActiveUsers activeUsers = new ActiveUsers();
        activeUsers.setId(id);
        return activeUsers;
    }
}
