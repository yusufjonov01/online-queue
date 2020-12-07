package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.Direction;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.enums.DirectionDateName;
import uz.ecma.queueserver.exception.BadRequestException;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqDirection;
import uz.ecma.queueserver.repository.CompanyRepository;
import uz.ecma.queueserver.repository.DirectionRepository;
import uz.ecma.queueserver.repository.RoleRepository;
import uz.ecma.queueserver.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.ecma.queueserver.entity.enums.RoleName.DIRECTOR;

@Service
public class DirectionService {
    @Autowired
    DirectionRepository directionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RoleRepository roleRepository;

    public ApiResponse addDirection(ReqDirection reqDirection, User user) {
        try {
            final User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            if (checkRole(getUser)) {
                boolean exists = directionRepository.existsByNameUzlEqualsIgnoreCaseOrNameUzkEqualsIgnoreCaseOrNameRuEqualsIgnoreCaseOrNameEnEqualsIgnoreCaseAndCompany(reqDirection.getNameUzl(), reqDirection.getNameUzk(), reqDirection.getNameRu(), reqDirection.getNameEn(), user.getCompany());
                if (exists) {
                    return new ApiResponse(
                            "Siz bunday bo'lim bor!",
                            "You have already created",
                            "Вы уже создали",
                            "Сиз аллақачон яратиб бўлгансиз", false
                    );
                }
                Direction direction = new Direction();
                direction.setCompany(getUser.getCompany());
                direction.setDirectionDateName(DirectionDateName.valueOf(reqDirection.getDirectionDateName()));
                Direction makeDirection = makeDirection(direction, reqDirection);
                directionRepository.save(makeDirection);
                return new ApiResponse(
                        "Muvaffaqiyatli saqlandi",
                        "Successfully saved",
                        "Успешно сохранено",
                        "Муваффақиятли сақланди",
                        true);
            }
            return new ApiResponse(
                    "Siz yarata olmaysiz",
                    "You can not create",
                    "ты не можешь создать",
                    "Сиз ярата олмайсиз", false
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

    public ApiResponse isActiveItem(Integer id, String requestType, User user) {
        try {
            for (Role role : user.getRoles()) {
                if (role.getAuthority().equals("DIRECTOR") ||
                        role.getAuthority().equals("MODERATOR")) {
                    switch (requestType) {
                        case "direction":
                            Direction direction = directionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
                            direction.setActive(!direction.isActive());
                            directionRepository.save(direction);
                            if (direction.isActive()) {
                                return new ApiResponse(
                                        "Bo'lim aktivlashtirildi",
                                        "Successfully edited",
                                        "Успешно отредактировано",
                                        "Муваффақиятли таҳрирланди", true
                                );
                            } else {
                                return new ApiResponse(
                                        "Bo'lim aktivmaslashtirildi ",
                                        "Successfully edited",
                                        "Успешно отредактировано",
                                        "Муваффақиятли таҳрирланди", true
                                );
                            }
                        case "reception":
                            break;
                    }
                }
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

    public ApiResponse editDirection(Integer id, ReqDirection reqDirection, User user) {
        try {
            for (Role role : user.getRoles()) {
                if (role.getAuthority().equals("DIRECTOR")) {
                    boolean exists = directionRepository.existsByNameUzlEqualsIgnoreCaseAndCompanyAndIdNot(reqDirection.getNameUzl(), user.getCompany(), id);
                    boolean exists1 = directionRepository.existsByNameUzkEqualsIgnoreCaseAndCompanyAndIdNot(reqDirection.getNameUzk(), user.getCompany(), id);
                    boolean exists2 = directionRepository.existsByNameRuEqualsIgnoreCaseAndCompanyAndIdNot(reqDirection.getNameRu(), user.getCompany(), id);
                    boolean exists3 = directionRepository.existsByNameEnEqualsIgnoreCaseAndCompanyAndIdNot(reqDirection.getNameEn(), user.getCompany(), id);
                    if (!(exists || exists1 || exists2 || exists3)) {
                        Direction direction = directionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
                        direction.setNameUzl(reqDirection.getNameUzl());
                        direction.setNameUzk(reqDirection.getNameUzk());
                        direction.setNameRu(reqDirection.getNameRu());
                        direction.setNameEn(reqDirection.getNameEn());
//                        direction.setDirectionDateName(reqDirection.getDirectionDateName().isEmpty() ? DirectionDateName.valueOf(reqDirection.getDirectionDateName()) : direction.getDirectionDateName());
                        directionRepository.save(direction);
                        return new ApiResponse(
                                "Muvaffaqiyatli tahrirlandi",
                                "Successfully edited",
                                "Успешно отредактировано",
                                "Муваффақиятли таҳрирланди", true
                        );
                    }
                    return new ApiResponse(
                            "Bunday bo'lim mavjud",
                            "You have already created",
                            "Вы уже создали",
                            "Сиз аллақачон яратиб бўлгансиз", false
                    );
                }
            }
            return new ApiResponse(
                    "Siz tahrirlay olmaysiz",
                    "You can not edit",
                    "Вы не можете редактировать",
                    "Сиз таҳрирлай олмайсиз", false
            );
        } catch (
                Exception e) {
            return new ApiResponse(
                    "Xato",
                    "Error",
                    "Ошибка",
                    "Хато",
                    false);
        }
    }

    public ApiResponse deleteDirection(Integer id, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            if (checkRole(getUser)) {
                directionRepository.deleteById(id);
                return new ApiResponse(
                        "Muvaffaqiyatli o`chirildi",
                        "Successfully deleted",
                        "Успешно удален",
                        "Муваффақиятли ўчирилди", true
                );
            }
            return new ApiResponse(
                    "Siz o`chira olmaysiz",
                    "You can not delete",
                    "Вы не можете удалить",
                    "Сиз ўчира олмайсиз", false
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

    public ApiResponse byCompany(UUID id) {
        return new ApiResponse(true, directionRepository.findAllByCompanyId(id));
    }

    private Direction makeDirection(Direction direction, ReqDirection reqDirection) {
        direction.setNameUzl(reqDirection.getNameUzl());
        direction.setNameUzk(reqDirection.getNameUzk());
        direction.setNameEn(reqDirection.getNameEn());
        direction.setNameRu(reqDirection.getNameRu());
        if (reqDirection.isAutoTime() && (reqDirection.getTime() == null || reqDirection.getTime() <= 0)) {
            throw new BadRequestException("Vaqtni kiriting");
        }
        direction.setAutoTime(reqDirection.isAutoTime());
        direction.setTime(reqDirection.getTime());
        return direction;
    }

    public boolean checkRole(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == DIRECTOR) {
                return true;
            }
        }
        return false;
    }
}
