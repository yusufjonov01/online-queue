package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.CompanyRepository;
import uz.ecma.queueserver.repository.QueueRepository;
import uz.ecma.queueserver.repository.RoleRepository;
import uz.ecma.queueserver.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ContactService contactService;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    SupervisorService supervisorService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

    public UserDetails getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
    }

    //Admin
    public Long getCompanyQueueStatus(UUID id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        return Long.valueOf(queueRepository.getQueue(company.getId(), "CLOSED") + " good )  ( bad " + queueRepository.getQueue(company.getId(), "CANCELED"));
    }

    //Admin uchun
    public ResCompany getCompany(Company company) {
        if (company.isActive()) {
            return new ResCompany(
                    company.getId(),
                    company.getName(),
                    company.getContact(),
                    company.getLogo(),
                    company.getTin(),
                    company.getCategory(),
                    company.getWorkTimes(),
                    company.isActive(),
                    (company.getRateAmount() / company.getCountRate())
            );
        }
        return new ResCompany();

    }

    public ApiResponse addRegister(ReqUser reqUser) {
        try {
            Optional<User> optionalUser = userRepository.findByPhoneNumber(reqUser.getPhoneNumber());
            if (!optionalUser.isPresent()) {
                userRepository.save(new User(
                        reqUser.getFirstName().isEmpty() ? null : reqUser.getFirstName(),
                        reqUser.getLastName().isEmpty() ? null : reqUser.getLastName(),
                        reqUser.getMiddleName().isEmpty() ? null : reqUser.getLastName(),
                        reqUser.getPhoneNumber(),
                        passwordEncoder.encode(reqUser.getPassword().isEmpty() ? "123" : reqUser.getPassword()),
                        reqUser.getRoles().isEmpty() ?
                                roleRepository.findAllByRoleNameIn(
                                        Collections.singletonList(RoleName.USER)
                                ) : reqUser.getRoles()
                ));

                return new ApiResponse("Ro'yxatdan o'tdingiz", "Sign up", "зарегистрироваться", "", true);
            }
            return new ApiResponse("Siz avval ruyxatdan utgansiz", "Error", "ошибка", "", false);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", "Error", "ошибка", "", false);
        }
    }

    //Admin
    public ResPageable getList(Integer page, Integer size, User user) {
        boolean a = false;
        for (Role role : user.getRoles()) {
            if (role.getRoleName().equals(RoleName.ADMIN)) {
                a = true;
            }
        }
        if (a) {
            Page<Company> companyPage = companyRepository.findAll(PageRequest.of(page, size));
            return new ResPageable(
                    page, size,
                    companyPage.getTotalPages(),
                    companyPage.getTotalElements(),
                    companyPage.getContent().stream().map(this::getCompany).collect(Collectors.toList())
            );
        }
        return null;
    }

    //Admin
    public ApiResponse editCompanyActive(UUID id, ReqCompany reqCompany) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        company.setActive(reqCompany.isActive());
        companyRepository.save(company);
        User user = userRepository.findByCompany_Id(company.getId());
        supervisorService.sendToCompanyEmail(user);
        return new ApiResponse("Muvaffaqiyatli tahrirlandi",
                "Successfully edited",
                "Успешно отредактировано",
                "Муваффақиятли таҳрирланди",
                true);
    }

    public ApiResponse addModerator(ReqUser reqUser, User user) {
        try {
            boolean a = false;
            if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                for (Role role : user.getRoles()) {
                    if (role.getRoleName().equals(RoleName.ADMIN)) {
                        a = true;
                    }
                }
                if (reqUser.getPassword().equals(reqUser.getPrePassword())) {
                    if (a) {
                        userRepository.save(
                                new User(
                                        reqUser.getFirstName(),
                                        reqUser.getLastName(),
                                        reqUser.getMiddleName(),
                                        reqUser.getPhoneNumber(),
                                        reqUser.getPassword(),
                                        roleRepository.findAllByRoleNameIn(
                                                Collections.singletonList(RoleName.MODERATOR)
                                        )
                                )
                        );
                        return new ApiResponse("Muvaffaqiyatli saqlandi",
                                "Successfully saved",
                                "Успешно сохранено",
                                "Муваффақиятли сақланди",
                                true
                        );
                    }
                } else {
                    return new ApiResponse("Parol va takror parol noto'g'ri!",
                            "Password and pre-password is not same!",
                            "Nevernaya parol i pavtor parol",
                            "Parol va takror parol noto'g'ri!",
                            true
                    );
                }
            }
            return new ApiResponse(
                    "Siz yarata olmaysiz",
                    "You can not create",
                    "ты не можешь создать",
                    "Сиз ярата олмайсиз",
                    false
            );
        } catch (Exception e) {
            return new ApiResponse(
                    "Xato",
                    "Error",
                    "Ошибка",
                    "Хато",
                    false);
        }
    }

    public ApiResponse editModerator(UUID id, ReqUser reqUser, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            for (Role role : getUser.getRoles()) {
                if (role.getRoleName().equals(RoleName.ADMIN)) {
                    Optional<User> optionalUser = userRepository.findById(id);
                    if (optionalUser.isPresent()) {
                        User user1 = optionalUser.get();
                        user1.setLastName(reqUser.getLastName());
                        user1.setFirstName(reqUser.getFirstName());
                        user1.setMiddleName(reqUser.getMiddleName());
                        user1.setPhoneNumber(reqUser.getPhoneNumber());
                        user1.setPassword(passwordEncoder.encode(reqUser.getPassword()));
                        userRepository.save(user1);
                        return new ApiResponse(
                                "Muvaffaqiyatli tahrirlandi",
                                "Successfully edited",
                                "Успешно отредактировано",
                                "Муваффақиятли таҳрирланди",
                                true
                        );
                    }
                }
            }
            return new ApiResponse(
                    "Sizda bunday huquq yo'q",
                    "You can not create",
                    "ты не можешь создать",
                    "Сиз ярата олмайсиз",
                    false
            );
        } catch (Exception e) {
            return new ApiResponse(
                    "Xato",
                    "Error",
                    "Ошибка",
                    "Хато",
                    false
            );
        }
    }

    public ApiResponse editPassword(ReqUser reqUser, User user) {
        if (reqUser.getPassword().equals(reqUser.getPrePassword())) {
            if (passwordEncoder.matches(reqUser.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(reqUser.getPassword()));
                userRepository.save(user);
                return new ApiResponse(
                        "Muvaffaqiyatli Tahrirlandi",
                        "Successfully edited",
                        "Успешно отредактировано",
                        "Муваффақиятли сақланди",
                        true
                );
            }
            return new ApiResponse("Encodlashda xatolik",
                    "Encryption error",
                    "Ошибка шифрования",
                    "Хато",
                    false
            );
        }
        return new ApiResponse("Takroriy parollarda xatolik",
                "Repeat password error",
                "Повторите ошибку пароля",
                "Takroriy parollarda xatolik",
                false
        );
    }

    public ApiResponse deleteModerator(UUID id, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            for (Role role : getUser.getRoles()) {
                if (role.getRoleName().equals(RoleName.ADMIN)) {
                    Optional<User> optionalUser = userRepository.findById(id);
                    if (optionalUser.isPresent()) {
                        userRepository.deleteById(id);
                        return new ApiResponse(
                                "Muvaffaqiyatli o`chirildi",
                                "Successfully deleted",
                                "Успешно удален",
                                "Муваффақиятли ўчирилди", true
                        );
                    }
                    return new ApiResponse(
                            "Bunday moderator mavjud emas",
                            "This moderator does not exist",
                            "Этот модератор не существует",
                            "Бундай модератор мавжуд эмас ",
                            false
                    );
                }
            }
            return new ApiResponse(
                    "Siz o`chira olmaysiz",
                    "You can not delete",
                    "Вы не можете удалить",
                    "Сиз ўчира олмайсиз",
                    false
            );
        } catch (Exception e) {
            return new ApiResponse(
                    "Xato",
                    "Error",
                    "Ошибка",
                    "Хато",
                    false
            );
        }
    }

}