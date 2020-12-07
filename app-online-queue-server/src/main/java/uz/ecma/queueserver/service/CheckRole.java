package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.exception.BadRequestException;
import uz.ecma.queueserver.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static uz.ecma.queueserver.entity.enums.RoleName.*;

@Service
public class CheckRole {
    @Autowired
    RoleRepository roleRepository;

    public boolean isDirector(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == DIRECTOR) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == ADMIN) {
                return true;
            }
        }
        return false;
    }

    public boolean isModerator(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == MODERATOR) {
                return true;
            }
        }
        return false;
    }

    public boolean isReception(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == RECEPTION) {
                return true;
            }
        }
        return false;
    }

    public boolean isOperator(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == OPERATOR) {
                return true;
            }
        }
        return false;
    }

    public boolean isUser(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == USER) {
                return true;
            }
        }
        return false;
    }

    public Set<Role> getRole(String roleName) {
        switch (roleName.toUpperCase()) {
            case "ADMIN":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Arrays.asList(
                                RoleName.ADMIN,
                                RoleName.MODERATOR)));
            case "MODERATOR":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Collections.singletonList(
                                RoleName.MODERATOR)));
            case "DIRECTOR":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Arrays.asList(
                                RoleName.DIRECTOR,
                                RoleName.MODERATOR)));
            case "OPERATOR":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Collections.singletonList(
                                OPERATOR)));
            case "RECEPTION":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Collections.singletonList(
                                RECEPTION)));
            case "USER":
                return roleRepository.findAllByRoleNameIn(new ArrayList<>(
                        Collections.singletonList(
                                USER)));
            default:
                throw new BadRequestException("RoleName is wrong");
        }
    }
}
