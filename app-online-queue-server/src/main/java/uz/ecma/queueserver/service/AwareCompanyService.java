package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.AwareCompany;
import uz.ecma.queueserver.entity.Contact;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqAwareCompany;
import uz.ecma.queueserver.repository.AwareCompanyRepository;
import uz.ecma.queueserver.repository.UserRepository;

import java.util.Optional;

@Service
public class AwareCompanyService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AwareCompanyRepository awareCompanyRepository;

    public ApiResponse addAwareCompany(ReqAwareCompany request, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            for (Role role : getUser.getRoles()) {
                if (role.getAuthority().equals("DIRECTOR")) {
                    awareCompanyRepository.save(new AwareCompany(
                            request.getAware(),
                            request.getContact(),
                            request.getAwareLink()
                    ));
                }
            }
            return new ApiResponse(
                    "Sizda tahrirlash huquqi yo'q",
                    "You can not edit",
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
}
