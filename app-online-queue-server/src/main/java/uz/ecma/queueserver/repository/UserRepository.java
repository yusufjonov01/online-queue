package uz.ecma.queueserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ResUser;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);


    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    List<User> findAllByRolesInAndCompanyId(Set<Role> roles, UUID company_id);

    @Query(value = "select * from users u inner join user_role r on u.id=r.user_id where r.role_id=:role_id", nativeQuery = true)
    List<User> getUsersByRole(Integer role_id);

    @Query(value = "select * from users where substring(phone_number, 2 )=:phoneNumber", nativeQuery = true)
    Optional<User> findUsersByPhoneNumber(String phoneNumber);

    @Query(value = "select count(*) from users u inner join user_role r on u.id=r.user_id where r.role_id=:role_id and company_id=:company_id", nativeQuery = true)
    Integer countAllByRolesAndCompany(UUID company_id, Integer role_id);

//    @RestResource(path = "getUserByRole")
//    @Query(value = "select * from users u inner join user_role r on u.id=r.user_id where r.role_id=:id", nativeQuery = true)
//    List<User> getUserByRole(@Param(value = "id") Integer id);

    // List<User> findAllByRole
    @Query(value = "select * from users u where u.first_name like concat('%','Admin','%')", nativeQuery = true)
    User searchByRoleForAdmin();

    User findByCompany_Id(UUID company_id);


}
