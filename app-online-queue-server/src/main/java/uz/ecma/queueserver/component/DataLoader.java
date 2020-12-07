package uz.ecma.queueserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.repository.RoleRepository;
import uz.ecma.queueserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.datasource.initialization-mode}")
    private String ketmon;

    @Override
    public void run(String... args) throws Exception {
        if (ketmon.equals("always")) {
            userRepository.save(new User(
                    "SuperAdmin",
                    "Admin",
                    "",
                    "+998901234567",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(new ArrayList<>(
                            Arrays.asList(
                                    RoleName.ADMIN,
                                    RoleName.MODERATOR)))
            ));
            userRepository.save(new User(
                    "Moderator",
                    "ModeratorBoy",
                    "",
                    "+998901234568",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(
                            Collections.singletonList(RoleName.MODERATOR))
            ));

            userRepository.save(new User(
                    "Director",
                    "DirectorBoy",
                    "",
                    "+998901234569",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(new ArrayList<>(
                            Arrays.asList(
                                    RoleName.DIRECTOR,
                                    RoleName.OPERATOR
                            )
                    ))
            ));

            userRepository.save(new User(
                    "Reception",
                    "ReceptionXon",
                    "",
                    "+998901234509",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(new ArrayList<>(
                            Arrays.asList(
                                    RoleName.RECEPTION,
                                    RoleName.USER
                            )
                    ))
            ));

            userRepository.save(new User(
                    "Operator",
                    "OperatorBoy",
                    "",
                        "+998901234560",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(
                            Collections.singletonList(
                                    RoleName.OPERATOR
                            )
                    )
            ));

            userRepository.save(new User(
                    "User",
                    "User",
                    "",
                    "+998901234565",
                    passwordEncoder.encode("123"),
                    roleRepository.findAllByRoleNameIn(new ArrayList<>(
                            Arrays.asList(
                                    RoleName.USER
                            )
                    ))
            ));




        }
    }
}

//class A{
//    public static void main(String[] args) {
//        String  b = new String("ketmon");
//        String a = new String("ketmon");
//        if (a.equals(b)){
//            System.out.println("yaxshi");
//        }else {
//            System.out.println(a instanceof Object);
//            System.out.println(b instanceof Object);
//            System.out.println(a instanceof String);
//            System.out.println(b instanceof String);
//            System.out.println("yomon");
//        }
//    }
//}
