package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Contact;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqContact;
import uz.ecma.queueserver.payload.ResContact;
import uz.ecma.queueserver.repository.ContactRepository;
import uz.ecma.queueserver.repository.UserRepository;
import uz.ecma.queueserver.repository.rest.DistrictRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    UserRepository userRepository;

    Contact addContact(ReqContact reqContact) {
//        if (contactRepository.existsByEmailEqualsIgnoreCase(reqContact.getEmail())) {
//            throw new ExistException("Bunday email mavjud");
//        }
        return contactRepository.save(new Contact(
                districtRepository.findById(reqContact.getDistrictId()).orElseThrow(() -> new ResourceNotFoundException("getDistrict")),
                reqContact.getAddress(),
                reqContact.getPhoneNumbers(),
                reqContact.getEmail(),
                reqContact.getFax(),
                reqContact.getLat(),
                reqContact.getLng(),
                reqContact.getAwareCompanies()
        ));

    }

    Contact editContact(UUID id, ReqContact reqContact) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setDistrict(districtRepository.findById(reqContact.getDistrictId()).orElseThrow(() -> new ResourceNotFoundException("getDistrict")));
            contact.setAddress(reqContact.getAddress());
            contact.setEmail(reqContact.getEmail());
            contact.setFax(reqContact.getFax());
            contact.setPhoneNumber(reqContact.getPhoneNumbers());
            contact.setLat(reqContact.getLat());
            contact.setLng(reqContact.getLng());
            contact.setAwareCompanies(reqContact.getAwareCompanies());
            return contactRepository.save(contact);
        }
        throw new ResourceNotFoundException("Bunday conact topilmadi");
    }

    public ApiResponse editContact(UUID id, ReqContact reqContact, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            for (Role role : getUser.getRoles()) {
                if (role.getAuthority().equals("DIRECTOR")) {
                    Optional<Contact> optionalContact = contactRepository.findById(id);
                    if (optionalContact.isPresent()) {
                        Contact contact = optionalContact.get();
                        contact.setEmail(reqContact.getEmail());
                        contact.setFax(reqContact.getFax());
                        contact.setPhoneNumber(reqContact.getPhoneNumbers());
                        contactRepository.save(contact);
                        return new ApiResponse(
                                "Muvaffaqiyatli tahrirlandi",
                                "Successfully editet",
                                "ты не можешь создать",
                                "Муваффақиятли таҳрирланди", true
                        );
                    }
                }
            }
            return new ApiResponse(
                    "Sizda tahrirlash huquqi yo'q",
                    "You can not edit",
                    "ты не можешь создать",
                    "Сиз ярата олмайсиз", false
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


    ResContact getContact(Contact contact) {
        return new ResContact(
                contact.getDistrict(),
                contact.getAddress(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getFax(),
                contact.getLat(),
                contact.getLng(),
                contact.getAwareCompanies()

        );
    }
}
