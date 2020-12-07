package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.queueserver.entity.Region;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.enums.RoleName;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Set<Role> findAllByRoleNameIn(Collection<RoleName> roleName);
}
