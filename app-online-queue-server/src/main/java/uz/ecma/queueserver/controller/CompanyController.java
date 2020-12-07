package uz.ecma.queueserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.queueserver.entity.Company;
import uz.ecma.queueserver.entity.Contact;
import uz.ecma.queueserver.entity.Role;
import uz.ecma.queueserver.entity.User;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.payload.*;
import uz.ecma.queueserver.repository.CategoryRepository;
import uz.ecma.queueserver.repository.CompanyRepository;
import uz.ecma.queueserver.repository.RoleRepository;
import uz.ecma.queueserver.repository.UserRepository;
import uz.ecma.queueserver.security.CurrentUser;
import uz.ecma.queueserver.service.CheckRole;
import uz.ecma.queueserver.service.CompanyService;
import uz.ecma.queueserver.service.ContactService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ContactService contactService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CheckRole checkRole;

    @PostMapping //working
    public HttpEntity<?> addCompany(@RequestBody ReqCompany reqCompany) {
        ApiResponse apiResponse = companyService.addCompany(reqCompany);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/{id}") //working
    public HttpEntity<?> getCompany(@PathVariable UUID id) {
        Company byId = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        ResCompany company = companyService.getCompany(byId);
        return ResponseEntity.ok(new ApiResponse(true, company));
    }

    @GetMapping("/companyByDirection/{id}")
    public HttpEntity<?> getCompanyByDirection(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse(true, companyRepository.getCompanyByDirectionId(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"))));
    }

    @PutMapping("/companyActive/{id}")//working
    public HttpEntity<?> companyActive(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = companyService.companyActive(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getCompanyOwner/{id}")//working
    public HttpEntity<?> getCompanyOwner(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse companyOwner = companyService.getCompanyOwner(id, user);
        return ResponseEntity.status(companyOwner.isSuccess() ? 200 : 409).body(companyOwner);
    }

    @GetMapping("/getCompanyStaffCount/{id}")//working
    public HttpEntity<?> getCompanyStaffCount(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse companyStaffCount = companyService.getCompanyCount(id, user);
        return ResponseEntity.status(companyStaffCount.isSuccess() ? 200 : 409).body(companyStaffCount);
    }

    @GetMapping("/getCategoryId")//working
    public HttpEntity<?> getTopCategoryId() {
        return ResponseEntity.ok(categoryRepository.getCategoryForIndex());
    }

    @GetMapping("/getCompanyListForIndex")
    public HttpEntity<?> getCompanyListForIndex() {
        ApiResponse companyListForIndex = companyService.getCompanyListForIndex();
        return ResponseEntity.status(companyListForIndex.isSuccess() ? 200 : 409).body(companyListForIndex);
    }

    @GetMapping("/byCategory/{id}")
    public HttpEntity<?> getCompanyListByCategory(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "6") Integer size, @PathVariable Integer id) {
        return ResponseEntity.ok(companyService.getCompanyListByCategory(page, size, id));
    }

    @GetMapping("/getCompanyForAdmin/{id}")
    public HttpEntity<?> getCompanyForAdmin(@PathVariable UUID id, @CurrentUser User user) {
        Company byId = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
        return ResponseEntity.ok(companyService.getCompanyForAdmin(byId, user));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editCompany(@PathVariable UUID id, @RequestBody ReqCompany reqCompany, @CurrentUser User user) {
        ApiResponse apiResponse = companyService.editCompany(id, reqCompany, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PatchMapping("/contact/{id}")
    public HttpEntity<?> editContact(@PathVariable UUID id, @RequestBody ReqContact reqContact, @CurrentUser User user) {
        ApiResponse apiResponse = contactService.editContact(id, reqContact, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getCompanies(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(companyService.getCompanies(page, size));
    }

    @GetMapping("/getAllCompany")
    public HttpEntity<?> getAllCompanies(
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size, @CurrentUser User user) {
        return ResponseEntity.ok(companyService.getAllCompanies(type, page, size, user));
    }

    @GetMapping("/category")
    public HttpEntity<?> getCategories() {
        return ResponseEntity.ok(companyService.getCategories());
    }

    @GetMapping("/search/{name}") //no working
    public HttpEntity<?> searchCompany(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size, @PathVariable String name) {

        return ResponseEntity.ok(companyService.getSearchedCompanies(page, size, name));
    }
}
