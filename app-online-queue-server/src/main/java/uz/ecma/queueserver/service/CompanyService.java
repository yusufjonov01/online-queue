package uz.ecma.queueserver.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.*;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.exception.BadRequestException;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.*;
import uz.ecma.queueserver.repository.rest.AwareRepository;
import uz.ecma.queueserver.repository.rest.DistrictRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContactService contactService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WorkTimeService workTimeService;
    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    WorkTimeRepository workTimeRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AwareCompanyRepository awareCompanyRepository;
    @Autowired
    CheckRole checkRole;
    @Autowired
    DistrictRepository districtRepository;

    public ApiResponse addCompany(ReqCompany reqCompany) {
        try {
            if (!userRepository.existsByPhoneNumber(reqCompany.getReqUser().getPhoneNumber())) {
                if (!reqCompany.getReqUser().getFirstName().equals("") && !reqCompany.getReqUser().getLastName().equals("")) {
                    if (reqCompany.getReqUser().getPassword().equals(reqCompany.getReqUser().getPrePassword())) {
                        if (!companyRepository.existsByNameEqualsIgnoreCase(reqCompany.getName())) {
                            if (!companyRepository.existsByTin(reqCompany.getTin())) {
                                Company company = new Company();
                                company.setName(reqCompany.getName());
                                company.setContact(contactService.addContact(reqCompany.getReqContact()));
                                company.setLogo(reqCompany.getLogoId() == null ? null : attachmentRepository.findById(reqCompany.getLogoId()).orElseThrow(() -> new ResourceNotFoundException("getLogo")));
                                company.setTin(reqCompany.getTin());
                                company.setCategory(categoryRepository.findById(reqCompany.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("getCategory")));
                                Company saveCompany = companyRepository.save(company);
                                User user = userRepository.save(new User(
                                        reqCompany.getReqUser().getFirstName(),
                                        reqCompany.getReqUser().getLastName(),
                                        reqCompany.getReqUser().getMiddleName(),
                                        reqCompany.getReqUser().getPhoneNumber(),
                                        passwordEncoder.encode(reqCompany.getReqUser().getPassword()),
                                        saveCompany,
                                        roleRepository.findAllByRoleNameIn(new ArrayList<>(
                                                Collections.singletonList(
                                                        RoleName.DIRECTOR)))));


                                Direction direction = new Direction();
                                direction.setNameEn("Direction");
                                direction.setNameRu("Отдел");
                                direction.setNameUzk("Bo'lim");
                                direction.setNameUzl("Булим");
                                direction.setCompany(saveCompany);
                                Direction savedDirection = directionRepository.save(direction);

                                List<String> weekDays = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
                                for (int i = 0; i < 7; i++) {
                                    ReqWorkTime workTime = new ReqWorkTime();
                                    workTime.setActive(i < 5);
                                    workTime.setLunchActive(i < 5);
                                    workTime.setCompanyId(saveCompany.getId());
                                    workTime.setStartTime("09:00");
                                    workTime.setFinishTime("18:00");
                                    workTime.setLunchStartTime("13:00");
                                    workTime.setLunchFinishTime("14:00");
                                    workTime.setWeekDay(weekDays.get(i));
                                    workTimeService.addWorkTime(workTime, user);
                                }

                                return new ApiResponse(
                                        "Muvaffaqiyatli saqlandi",
                                        "Successfully saved",
                                        "Успешно сохранено",
                                        "Муваффақиятли сакланди", true,
                                        getCompany(saveCompany)
                                );

                            } else {
                                return new ApiResponse("Bunday STIR mavjud ", "This TIN already exists.", "Есть такая ИНН", "Бундай СТИР мавжуд", false);
                            }
                        } else {
                            return new ApiResponse("Bunday brand mavjud", "This brand already exists.", "Этот бренд уже существует", "Бундай бранд мавжуд", false);
                        }

                    } else {
                        return new ApiResponse("Parollar mos emas", "Passwords do not match", "Пароли не совпадают", "Пароллар мос эмас", false);
                    }
                } else {
                    return new ApiResponse("Bo'sh qatorlarni to'ldiring", "Fill in the blank fields", "Заполните пустые поля", "Бўш қаторларни тўлдиринг", false);
                }
            }
            return new ApiResponse("Bunday raqam ro'yhatdan o'tqazilgan ", "This number is already registered", "Этот номер уже зарегистрирован", "Бундай рақам рўйҳатдан ўтқазилган", false);

        } catch (Exception e) {
            return new ApiResponse("Tarmoqda xatolik", "Network error", "Ошибка сети", "Тармоқда хатолик", false);
        }

    }

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
                    company.getRateAmount() == null ? null : (company.getRateAmount() / company.getCountRate())
            );
        }
        return new ResCompany();
    }

    public ResCompany getAllCompany(Company company) {
        return new ResCompany(
                company.getCreatedAt(),
                company.getCreatedBy(),
                company.getUpdateBy(),
                company.getUpdatedAt(),
                company.getCountRate(),
                company.getId(),
                company.getName(),
                company.getContact(),
                company.getLogo(),
                company.getTin(),
                company.getCategory(),
                company.getWorkTimes(),
                company.isActive(),
                company.getRateAmount() == null ? null : (company.getRateAmount() / company.getCountRate())
        );
    }

    public ApiResponse getCompanyOwner(UUID company_id, User user) {
        if (checkRole.isAdmin(user) || checkRole.isModerator(user)) {
            Set<Role> roleSet = roleRepository.findAllByRoleNameIn(Collections.singleton(RoleName.DIRECTOR));
            List<User> companyOwner = userRepository.findAllByRolesInAndCompanyId(roleSet, company_id);
            return new ApiResponse(true, companyOwner);
        }
        return new ApiResponse(false, null);
    }

    public ApiResponse getCompanyCount(UUID id, User user) {
        try {
            if (checkRole.isDirector(user) || checkRole.isAdmin(user) || checkRole.isModerator(user)) {
                Integer operatorCount = userRepository.countAllByRolesAndCompany(id, 3);
                Integer receptionCount = userRepository.countAllByRolesAndCompany(id, 2);
                Integer directionCount = directionRepository.countAllByCompany_Id(id);
                Integer allStaff = operatorCount + receptionCount + 1;
                JSONObject companyCount = new JSONObject();
                companyCount.put("operatorCount", operatorCount);
                companyCount.put("receptionCount", receptionCount);
                companyCount.put("directionCount", directionCount);
                companyCount.put("allStaff", allStaff);
                return new ApiResponse(true, companyCount);
            }
            return new ApiResponse(false, null);
        } catch (Exception e) {
            return new ApiResponse(false, null);
        }
    }

    public ApiResponse companyActive(UUID id, User user) {
        try {
            if (checkRole.isModerator(user) || checkRole.isAdmin(user)) {
                Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompanyForActive"));
                company.setActive(!company.isActive());
                companyRepository.save(company);
                if (company.isActive()) {
                    return new ApiResponse(
                            "Kompaniya aktivlashtirildi",
                            "Successfully edited",
                            "Успешно отредактировано",
                            "Муваффақиятли таҳрирланди", true
                    );
                } else {
                    return new ApiResponse(
                            "Kopmaniya aktivmaslashtirildi ",
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

    public ResCompany getCompanyForAdmin(Company company, User user) {
        boolean isAdmin = false;
        for (Role role : user.getRoles()) {
            if (role.getRoleName().toString().equals("ADMIN")) isAdmin = true;
        }
        if (isAdmin) {
            return new ResCompany(
                    company.getId(),
                    company.getName(),
                    company.getContact(),
                    company.getLogo(),
                    company.getTin(),
                    company.getCategory(),
                    company.getWorkTimes(),
                    company.isActive(),
                    company.getRateAmount() == null ? null : (company.getRateAmount() / company.getCountRate())
            );
        }
        return new ResCompany();

    }

    public ApiResponse getCompanyListForIndex() {
//        List<Category> categoryForIndex = categoryRepository.getCategoryForIndex();
//        for (int i = 0; i < categoryForIndex.size(); i++) {
//            return null;
//        }
        return null;
    }

    public ApiResponse getCompanyListByCategory(Integer page, Integer size, Integer id) {
        try {
            if (page < 0) {
                throw new BadRequestException("Page 0 dan kichik bo'lishi mumkin emas");
            }
            if (size < 1) {
                throw new BadRequestException("Size 1 dan kichik bo'lishi mumkin emas");
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companyPage = companyRepository.findAllByCategoryIdAndActive(id, true, pageable);
            return new ApiResponse("Company", "Company", "Company", "Company", true, new ResPageable(
                    page, size, companyPage.getTotalPages(), companyPage.getTotalElements(), companyPage.getContent().stream().map(this::getCompany).collect(Collectors.toList())
            ));
        } catch (IllegalArgumentException e) {
            return new ApiResponse(e.getMessage(), e.getMessage(), e.getMessage(), e.getMessage(), false);
        }
    }

    public ApiResponse editCompany(UUID id, ReqCompany reqCompany, User user) {
        try {
            User getUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("getUser"));
            if (checkRole.isDirector(user) || checkRole.isModerator(user)) {
                boolean isTrue = false;
                Optional<Contact> optionalContact = contactRepository.findById(reqCompany.getReqContact().getId());
                Contact contact = new Contact();
                if (optionalContact.isPresent()) {
                    contact = optionalContact.get();
                }
                switch (reqCompany.getRequestType()) {
                    case "contact":
                        contact.setEmail(reqCompany.getReqContact().getEmail());
                        contact.setFax(reqCompany.getReqContact().getFax());
                        contact.setPhoneNumber(reqCompany.getReqContact().getPhoneNumbers());
                        List<AwareCompany> awareCompanyList = new ArrayList<>();
                        if (reqCompany.getReqContact().getAwareCompanies().size() > 0) {
                            for (int i = 0; i < reqCompany.getReqContact().getAwareCompanies().size(); i++) {
                                Optional<AwareCompany> awareCompanyOptional = awareCompanyRepository.findByContactIdAndAwareId(getUser.getCompany().getContact().getId(), reqCompany.getReqContact().getAwareCompanies().get(i).getAware().getId());
                                if (awareCompanyOptional.isPresent()) {
                                    AwareCompany awareCompany = awareCompanyOptional.get();
                                    awareCompany.setAwareLink(reqCompany.getReqContact().getAwareCompanies().get(i).getAwareLink());
                                    awareCompanyRepository.save(awareCompany);
                                    awareCompanyList.add(awareCompany);
                                } else {
                                    AwareCompany awareCompany = awareCompanyRepository.save(new AwareCompany(
                                            reqCompany.getReqContact().getAwareCompanies().get(i).getAware(),
                                            getUser.getCompany().getContact(),
                                            reqCompany.getReqContact().getAwareCompanies().get(i).getAwareLink()
                                    ));
                                    awareCompanyList.add(awareCompany);
                                }
                            }
                        }
                        contact.setAwareCompanies(awareCompanyList.size() > 0 ? awareCompanyList : null);
                        contactRepository.save(contact);
                        isTrue = true;
                        break;
                    case "company":
                        if (checkRole.isModerator(user)) {
                            Optional<Company> optionalCompany = companyRepository.findById(id);
                            if (optionalCompany.isPresent()) {
                                contact.setDistrict(districtRepository.findById(reqCompany.getReqContact().getDistrictId()).orElseThrow(() -> new ResourceNotFoundException("getCompanyDistrict")));
                                contact.setLat(reqCompany.getReqContact().getLat());
                                contact.setLng(reqCompany.getReqContact().getLng());
                                contact.setAddress(reqCompany.getReqContact().getAddress());
                                contactRepository.save(contact);
                                Company company = optionalCompany.get();
                                company.setCategory(categoryRepository.findById(reqCompany.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("getCompanyCategory")));
                                company.setTin(reqCompany.getTin());
                                company.setName(reqCompany.getName());
                                company.setLogo(reqCompany.getLogoId() == null ? attachmentRepository.findById(company.getLogo().getId()).orElseThrow(() -> new ResourceNotFoundException("getLogo"))
                                        : attachmentRepository.findById(reqCompany.getLogoId()).orElseThrow(() -> new ResourceNotFoundException("getCompanyNewLogoId")));
                                companyRepository.save(company);
                                isTrue = true;
                            }
                        }
                        break;
                    case "aware":

                        break;
                }
                if (isTrue) {
                    return new ApiResponse(
                            "Muvaffaqiyatli tahrirlandi",
                            "Successfully editet",
                            "ты не можешь создать",
                            "Муваффақиятли таҳрирланди", true
                    );
                } else {
                    return new ApiResponse(
                            "Bunday ma'lumot topilmadi",
                            "Эта информация не может найти",
                            "ты не можешь создать",
                            "Сиз ярата ", false
                    );
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

    public ApiResponse getCompanies(Integer page, Integer size) {
        try {
            if (page < 0) {
                throw new BadRequestException("Page 0 dan kichik bo'lishi mumkin emas");
            }
            if (size < 1) {
                throw new BadRequestException("Size 1 dan kichik bo'lishi mumkin emas");
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companyPage = (Page<Company>) companyRepository.findAll(pageable);
            return new ApiResponse(
                    "Mana Yangi Kampaniyalar",
                    "Here are the new campaigns",
                    "Вот новые кампании",
                    "Мана Янги Кампаниялар",
                    true, new ResPageable(
                    page, size, companyPage.getTotalPages(),
                    companyPage.getTotalElements(),
                    companyPage.getContent().stream().map(this::getCompany).collect(Collectors.toList())
            ));
        } catch (IllegalArgumentException e) {
            return new ApiResponse(e.getMessage(), e.getMessage(), e.getMessage(), e.getMessage(), false);
        }
    }

    public ApiResponse getAllCompanies(String type, Integer page, Integer size, User user) {

        try {
            if (checkRole.isAdmin(user) || checkRole.isModerator(user)) {
                if (page < 0) {
                    throw new BadRequestException("Page 0 dan kichik bo'lishi mumkin emas");
                }
                if (size < 1) {
                    throw new BadRequestException("Size 1 dan kichik bo'lishi mumkin emas");
                }
                Pageable pageable = PageRequest.of(page, size);
                Page<Company> companyPage = (Page<Company>) companyRepository.findAll(pageable);
                if (type.equals("active")) {
                    companyPage = (Page<Company>) companyRepository.findAllByActiveOrderByCreatedAt(true, pageable);
                } else if (type.equals("new")) {
                    companyPage = (Page<Company>) companyRepository.findAllByActiveOrderByCreatedAt(false, pageable);
                }
                return new ApiResponse(
                        "Mana Yangi Kampaniyalar",
                        "Here are the new campaigns",
                        "Вот новые кампании",
                        "Мана Янги Кампаниялар",
                        true, new ResPageable(
                        page, size, companyPage.getTotalPages(),
                        companyPage.getTotalElements(),
                        companyPage.getContent().stream().map(this::getAllCompany).collect(Collectors.toList())
                ));
            } else {
                throw new BadRequestException("Sizda bunday huquq yo'q");
            }
        } catch (IllegalArgumentException e) {
            return new ApiResponse(e.getMessage(), e.getMessage(), e.getMessage(), e.getMessage(), false);
        }
    }

    public ApiResponse getCategories() {
        try {
            List<Category> categories = categoryRepository.getCategoryWithCountForIndex();
            return new ApiResponse("Mana Kategoriyalar", "Here are categories", "Вот новые категория", "Мана Янги категория", true, categories
            );
        } catch (IllegalArgumentException e) {
            return new ApiResponse(e.getMessage(), e.getMessage(), e.getMessage(), e.getMessage(), false);
        }
    }

    public ApiResponse getSearchedCompanies(Integer page, Integer size, String name) {
        try {
            if (page < 0) {
                throw new BadRequestException("Page 0 dan kichik bo'lishi mumkin emas");
            }
            if (size < 1) {
                throw new BadRequestException("Size 1 dan kichik bo'lishi mumkin emas");
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companyPage = companyRepository.findAllByNameContains(name, pageable);
            return new ApiResponse("Company", "Company", "Company", "Company", true, new ResPageable(
                    page, size, companyPage.getTotalPages(), companyPage.getTotalElements(), companyPage.getContent().stream().map(this::getCompany).collect(Collectors.toList())
            ));
        } catch (IllegalArgumentException e) {
            return new ApiResponse(e.getMessage(), e.getMessage(), e.getMessage(), e.getMessage(), false);
        }
    }
}
