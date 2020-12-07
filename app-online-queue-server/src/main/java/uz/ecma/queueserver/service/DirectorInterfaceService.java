package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import uz.ecma.queueserver.entity.*;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.entity.enums.WeekName;
import uz.ecma.queueserver.exception.BadRequestException;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DirectorInterfaceService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckRole checkRole;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    OperatorDirectionRepository operatorDirectionRepository;
    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    WorkTimeRepository workTimeRepository;
    @Autowired
    CompanyRepository companyRepository;

    public ApiResponse addStaff(ReqUser reqUser, User user) {
        User currentUser = getUser(user.getId());
        if (!reqUser.getPassword().equals(reqUser.getPrePassword())) {
            return new ApiResponse(
                    "Parollar nomutanosib",
                    "Passwords is not same",
                    "Пароли не совпадают",
                    "Пароллар номутаносиб", false
            );
        }
        if (checkRole.isDirector(currentUser)) {
            if (!userRepository.findByPhoneNumber(reqUser.getPhoneNumber()).isPresent()) {
                if (reqUser.getFirstName() != null &&
                        reqUser.getLastName() != null &&
                        reqUser.getPassword() != null &&
                        reqUser.getRole() != null) {
                    User newStaff = new User(
                            reqUser.getFirstName(),
                            reqUser.getLastName(),
                            reqUser.getMiddleName(),
                            reqUser.getPhoneNumber(),
                            passwordEncoder.encode(reqUser.getPassword()),
                            checkRole.getRole(reqUser.getRole())
                    );
                    newStaff.setCompany(currentUser.getCompany());
                    User save = userRepository.save(newStaff);
                    if (reqUser.getRole().equals(RoleName.OPERATOR.toString())) {
                        operatorDirectionRepository.save(new OperatorDirection(save, directionRepository.findById(reqUser.getDirectionId())
                                .orElseThrow(() -> new ResourceNotFoundException("getDirection")), true));
                    }
                    return new ApiResponse(
                            "Muvaffaqiyatli qo'shildi",
                            "Successfully added",
                            "Успешно добавлено",
                            "Муваффақиятли қошилди", true
                    );
                }
                return new ApiResponse(
                        "Хatolik ma'lumotlar to'liq emas",
                        "failed. Invalid data",
                        "Информация не до конца",
                        "Хатолик маълумотлар тўлиқ эмас", false);
            }
            return new ApiResponse(
                    "bunday nomdagi user serverda mavjud",
                    "failed. there is such user phone number on server",
                    "Такой пользователь в сервере уже есть",
                    "бундай номдаги усэр сэрвэрда мавжуд", false);
        }
        return new ApiResponse(
                "Man etilgan",
                "forbidden",
                "Запрешено",
                "Ман этилган", false);
    }

    public ApiResponse editStaff(ReqUser reqUser, User user, UUID staffId) {
        try {
            if (checkRole.isDirector(user)) {
                if (reqUser.getPassword() != null && reqUser.getPrePassword() != null) {
                    if (!(reqUser.getPassword().equals(reqUser.getPrePassword()))) {
                        return new ApiResponse(
                                "Parollar nomutanosib",
                                "Passwords is not same",
                                "Пароли не совпадают",
                                "Пароллар номутаносиб", false

                        );
                    }
                }
                User staff = getUser(staffId);
                if (userRepository.existsByPhoneNumberAndIdNot(reqUser.getPhoneNumber(), staffId)) {
                    return new ApiResponse(
                            "Bu raqam band",
                            "This phone number is busy",
                            "Этот номер телефона занят",
                            "Бу рақам банд", false
                    );
                }
                if ((reqUser.getRole().equals(RoleName.OPERATOR.toString()) || reqUser.getRole().equals(RoleName.RECEPTION.toString()))
                        && reqUser.getFirstName() != null &&
                        reqUser.getLastName() != null &&
                        reqUser.getRole() != null) {
                    staff.setFirstName(reqUser.getFirstName());
                    staff.setLastName(reqUser.getLastName());
                    staff.setMiddleName(reqUser.getMiddleName());
                    if (reqUser.getPassword() != null) {
                        staff.setPassword(passwordEncoder.encode(reqUser.getPassword()));
                    }
                    staff.setRoles(checkRole.getRole(reqUser.getRole()));
                    staff.setPhoneNumber(reqUser.getPhoneNumber());
                    User save = userRepository.save(staff);
                    if (reqUser.getRole().equals(RoleName.OPERATOR.toString())) {
                        if (!operatorDirectionRepository.existsByUserId(save.getId())) {
                            operatorDirectionRepository.save(
                                    new OperatorDirection
                                            (save, directionRepository.findById(reqUser.getDirectionId())
                                                    .orElseThrow(() -> new ResourceNotFoundException("getDirection")), true));
                        } else {
                            OperatorDirection operatorDirection = operatorDirectionRepository.findByUserId(save.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
                            operatorDirection.setDirection(directionRepository.findById(reqUser.getDirectionId()).orElseThrow(() -> new ResourceNotFoundException("getDirectionId")));
                            operatorDirectionRepository.save(operatorDirection);
                        }


                    }
                    return new ApiResponse(
                            "Muvaffaqiyatli tahrirlandi",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди",
                            true, save);
                }
                return new ApiResponse(
                        "Xato",
                        "Error",
                        "Ошибка",
                        "Хато",
                        false);
            }
            return new ApiResponse(
                    "Siz tahrirlay olmaysiz",
                    "You can not edit",
                    "Вы не можете редактировать",
                    "Сиз таҳрирлай олмайсиз", false
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

    public List<User> getListStaff(User user, String type) {
        User director = getUser(user.getId());
        if (director.getRoles().equals(roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.DIRECTOR)))) {
            switch (type) {
                case "reception":
                    return userRepository.findAllByRolesInAndCompanyId(roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.RECEPTION)), user.getCompany().getId());
                case "operator":
                    return userRepository.findAllByRolesInAndCompanyId(roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.OPERATOR)), user.getCompany().getId());
            }
        }
        throw new BadRequestException("You can not use");

    }

    public ApiResponse getStaff(User user) {
        try {
            User isUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            if (isUser.getRoles().equals(roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.DIRECTOR)))) {
                return new ApiResponse(true, new ResUser(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getMiddleName(),
                        user.getCompany(),
                        new ArrayList<>(user.getRoles()),
                        user.getPhoneNumber()
                ));
            }
            return new ApiResponse(
                    "Sizda bunday huquq yo'q",
                    "You may not show",
                    "Вы не можете смотреть",
                    "Сиз кура олмасиз", false
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

    public ApiResponse deleteStaff(UUID id, User user) {
        try {
            User isUser = getUser(user.getId());
            if (isUser.getRoles().equals(roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.DIRECTOR)))) {
                if (checkRole.isOperator(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDeleteUser")))) {
                    OperatorDirection operatorDirection = operatorDirectionRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("getDeleteOperatorDirection"));
                    operatorDirectionRepository.deleteById(operatorDirection.getId());
                }
                userRepository.deleteById(id);
                return new ApiResponse(
                        "Muvaffaqiyatli o'chirildi",
                        "Successfully deleted",
                        "Успешно удалённо",
                        "Муваффақиятли ўчирилди", true
                );
            }
            return new ApiResponse(
                    "O'chira olmaysiz",
                    "You may not delete",
                    "Вы не можете удалить",
                    "ўчира олмайсиз", false
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

    public ApiResponse onOffOperator(UUID id, User user) {
        try {
            if (checkRole.isDirector(user) || checkRole.isReception(user)) {
                OperatorDirection operatorDirection = operatorDirectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getOperator"));
                operatorDirection.setActive(!operatorDirection.isActive());
                operatorDirectionRepository.save(operatorDirection);
                if (operatorDirection.isActive()) {
                    return new ApiResponse(
                            "Xodim aktivlashtirildi",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди", true
                    );
                } else {
                    return new ApiResponse(
                            "Xodim aktivmaslashtirildi ",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди", true
                    );
                }
            }
            return new ApiResponse(
                    "Sizga ko'ra olmaysiz",
                    "You may not show",
                    "Вы не можете смотреть",
                    "Сиз кура олмасиз", false
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

    public ApiResponse isActiveStaff(UUID id, String requestType, User user) {
        try {
            boolean isSuccess = false;
            boolean isActive = false;
            switch (requestType) {
                case "reception":
                case "operator":
                    if (checkRole.isDirector(user)) {
                        User staff = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getStaff"));
                        if (staff.getCompany().getId().equals(user.getCompany().getId())) {
                            staff.setEnabled(!staff.isEnabled());
                            userRepository.save(staff);
                            isActive = staff.isEnabled();
                            isSuccess = true;
                        }
                    }
                    break;
                case "operatorDirection":
                    if (checkRole.isDirector(user) || checkRole.isReception(user)) {
                        OperatorDirection operatorDirection = operatorDirectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getOperator"));
                        operatorDirection.setActive(!operatorDirection.isActive());
                        operatorDirectionRepository.save(operatorDirection);
                        isActive = operatorDirection.isActive();
                        isSuccess = true;
                    }
                    break;
            }
            if (isSuccess) {
                if (isActive) {
                    return new ApiResponse(
                            "Xodim aktivlashtirildi",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди", true
                    );
                } else {
                    return new ApiResponse(
                            "Xodim aktivmaslashtirildi ",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди", true
                    );
                }
            }
            return new ApiResponse(
                    "Siz bunday huquq yo'q",
                    "You can not edit",
                    "Вы не можете редактировать",
                    "Сиз таҳрирлай олмайсиз", false
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

    public ResOperatorDirection resOperatorDirection(OperatorDirection operatorDirection) {
        return new ResOperatorDirection(
                operatorDirection.getId(),
                operatorDirection.getUser().getId(),
                operatorDirection.getUser().getFirstName(),
                operatorDirection.getUser().getLastName(),
                operatorDirection.getUser().getMiddleName(),
                operatorDirection.getUser().getPhoneNumber(),
                operatorDirection.isActive(),
                operatorDirection.getUser().isEnabled(),
                operatorDirection.getUser().getRateAmount(),
                operatorDirection.getUser().getCountRate(),
                operatorDirection.getUser().getCreatedAt(),
                operatorDirection.getUser().getUpdateBy(),
                operatorDirection.getUser().getUpdatedAt(),
                operatorDirection.getUser().getCompany().getId(),
                new ArrayList<>(operatorDirection.getUser().getRoles()),
                operatorDirection.getDirection()
        );
    }

    public ApiResponse addWorkTime(List<ReqWorkTime> workTimes, User user) {
        try {
            if (checkRole.isDirector(user)) {
                for (ReqWorkTime workTime : workTimes) {
                    boolean exists;
                    if (workTime.getCompanyId() == null) {
                        exists = workTimeRepository.existsByDirection_IdAndWeekName(workTime.getDirectionId(), WeekName.valueOf(workTime.getWeekDay()));
                    } else {
                        exists = workTimeRepository.existsByCompany_IdAndWeekName(workTime.getCompanyId(), WeekName.valueOf(workTime.getWeekDay()));
                    }
                    if (!exists) {
                        workTimeRepository.save(
                                new WorkTime(
                                        workTime.getStartTime(),
                                        workTime.getFinishTime(),
                                        workTime.isLunchActive(),
                                        workTime.getLunchStartTime(),
                                        workTime.getLunchFinishTime(),
                                        workTime.getDirectionId() == null ? null : directionRepository.findById(workTime.getDirectionId()).orElseThrow(() -> new ResourceNotFoundException("getDirection")),
                                        workTime.getCompanyId() == null ? null : companyRepository.findById(workTime.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("getCompan")),
                                        WeekName.valueOf(workTime.getWeekDay()),
                                        workTime.isActive()
                                )
                        );
                    } else {
                        Optional<WorkTime> opetionalWorkTime;
                        if (workTime.getCompanyId() == null) {
                            opetionalWorkTime = workTimeRepository.findByDirection_IdAndWeekName(workTime.getDirectionId(), WeekName.valueOf(workTime.getWeekDay()));
                        } else {
                            opetionalWorkTime = workTimeRepository.findByCompany_IdAndWeekName(workTime.getCompanyId(), WeekName.valueOf(workTime.getWeekDay()));
                        }
                        WorkTime saveWorkTime = opetionalWorkTime.orElseThrow(() -> new ResourceNotFoundException("getWorktime"));
                        saveWorkTime.setActive(workTime.isActive());
                        saveWorkTime.setStartTime(workTime.getStartTime());
                        saveWorkTime.setFinishTime(workTime.getFinishTime());
                        saveWorkTime.setLunchActive(workTime.isLunchActive());
                        saveWorkTime.setLunchFinishTime(workTime.getLunchFinishTime());
                        saveWorkTime.setLunchStartTime(workTime.getLunchStartTime());
                        workTimeRepository.save(saveWorkTime);
                    }
                }
                return new ApiResponse(
                        "Ish vaqti saqlandi",
                        "Hours saved",
                        "Часы сохранены",
                        "Ish vaqti saqlandi",
                        true);
            }
            return new ApiResponse("Sizda bunday role yo'q", "You do not have time ", "У тебя нет времени", "Sizda ish vaqti yuq", false);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", workTimes);
        }
    }

    public ApiResponse getOperatorListRes(UUID id) {
        try {
            List<OperatorDirection> operatorList = operatorDirectionRepository.getOperatorList(id);
            List<ResOperatorDirection> operatorDirectionList = operatorList.stream().map(this::resOperatorDirection).collect(Collectors.toList());
            return new ApiResponse(true, operatorDirectionList);
        } catch (Exception e) {
            return new ApiResponse(
                    "Xato",
                    "Error",
                    "Ошибка",
                    "Хато",
                    false);
        }
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
    }
}
