package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.*;
import uz.ecma.queueserver.payload.ReqUserNames;
import uz.ecma.queueserver.repository.*;

import java.util.Date;
import java.util.List;


@Service
public class SupervisorService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ComplainRepository complainRepository;
    @Autowired
    DirectionRepository directionRepository;
    @Autowired
    OperatorDirectionRepository operatorDirectionRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;


    public void companySupervisor() {
        List<Complains> complains = complainRepository.findAll();
        List<Company> companyByComplainHas = companyRepository.getCompanyByComplainsHas();
        List<Direction> directionByComplainHas = directionRepository.getDirectionByComplainsHas();

        // Company larni tekshirish
        for (Company company : companyByComplainHas) {
            Company spamCompany = companyRepository.findById(company.getId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
            if (spamCompany.isActive()) {
                spamCompany.setActive(false);

                Message message = new Message();
                message.setMessageText("Sizning barcha online navbat hizmatlaringiz blocklandi ! " +
                        "Sababi " + spamCompany.getName() +
                        " Shunday nomli bo'lim ochilganligi yoki taxrirlanganligi sababli sababli."
                );
                User byCompanyId = userRepository.findByCompany_Id(spamCompany.getId());
                message.setUser(byCompanyId);

                Message savedMessage = messageRepository.save(message);
                companyRepository.save(spamCompany);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("\uD83D\uDCDB Sizning Companiyangizda qoidalarimizga zid bo'lgan malumotlar topildi !!!");
                mailMessage.setText("");
                mailMessage.setText("Sizning barcha online navbat hizmatlaringiz blocklandi \uD83D\uDD12 !"
                        + "\n" + "Sababi -> " + spamCompany.getName() + " Shunday nomli Companiya ochilganligi  yoki taxrirlanganligi sababli. "
                        + "\n" + "All your online queue services are blocked! " + "\n" +
                        "Все ваши онлайн-сервисы очередей заблокированы!");
                mailMessage.setTo(spamCompany.getContact().getEmail());
                javaMailSender.send(mailMessage);


                // ADMIN ga habar berish
                User user = userRepository.searchByRoleForAdmin();
                Message messageForAdmin = new Message();
                messageForAdmin.setMessageText(spamCompany.getName() + " ning barcha online navbat hizmatlaringiz blocklandi ! " +
                        "Sababi " + spamCompany.getName() +
                        " Shunday nomli bo'lim ochilganligi yoki taxrirlanganligi sababli.");
                messageForAdmin.setUser(user);
                messageRepository.save(messageForAdmin);
            }
        }


        // Direction larni tekshirish
        for (Direction direction : directionByComplainHas) {
            Direction spamDirection = directionRepository.findById(direction.getId()).orElseThrow(() -> new ResourceNotFoundException("getCompany"));
            if (spamDirection.isActive()) {
                spamDirection.setActive(false);
                directionRepository.save(spamDirection);

                // Cabinet uchun xabar
                Message message = new Message();
                message.setMessageText("Sizning " + spamDirection.getNameUzl() + " online navbat hizmatlaringiz blocklandi ! " +
                        "Sababi " + spamDirection.getNameUzl() + " , " + spamDirection.getNameUzk() + " , " + spamDirection.getNameRu() + " , " + spamDirection.getNameEn() +
                        " Shunday nomli bo'lim ochilganligi yoki taxrirlanganligi sababli."
                );
                User byCompanyId = userRepository.findByCompany_Id(spamDirection.getCompany().getId());
                message.setUser(byCompanyId);
                messageRepository.save(message);

                // Mail uchun xabar
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("\uD83D\uDCDB Sizning Companiyangizda qoidalarimizga zid bo'lgan malumotlar topildi !!!");
                mailMessage.setText(
                        "Sizning barcha online navbat hizmatlaringiz blocklandi \uD83D\uDD12 !"
                                + "\n" +
                                "Sababi " + spamDirection.getNameUzl() + " , " + spamDirection.getNameUzk() + " , " + spamDirection.getNameRu() + " , " + spamDirection.getNameEn()
                                + "\n" +
                                "Shunday nomli bo'lim ochilganligi yoki taxrirlanganligi sababli sababli."
                                + "\n" +
                                "All your online queue services are blocked!"
                                + "\n" +
                                "Все ваши онлайн-сервисы очередей заблокированы!");
                mailMessage.setTo(spamDirection.getCompany().getContact().getEmail());
                javaMailSender.send(mailMessage);

                // ADMIN ga habar berish
                User user = userRepository.searchByRoleForAdmin();
                Message messageForAdmin = new Message();
                messageForAdmin.setMessageText(spamDirection.getCompany().getName() + " ning " + spamDirection.getNameUzl() + " bo'limining barcha online navbat hizmatlaringiz blocklandi ! " +
                        "Sababi " + spamDirection.getNameUzl() +
                        " Shunday nomli bo'lim ochilganligi yoki taxrirlanganligi sababli.");
                messageForAdmin.setUser(user);
                messageRepository.save(messageForAdmin);
            }
        }
    }

    // Operatorlarni qo'shish va tahrirlash paytida ismlarini tekshirish
    public boolean checkOperatorName(ReqUserNames req) {
        List<Complains> complains = complainRepository.findAll();

        boolean firstBoo = req.getFirstName() == null || req.getFirstName().isEmpty();
        boolean lastBoo = req.getLastName() == null || req.getLastName().isEmpty();
        boolean middleBoo = req.getMiddleName() == null || req.getMiddleName().isEmpty();

        String first = "";
        String last = "";
        String middle = "";

        if (firstBoo) {
            first = "string";
        } else {
            first = req.getFirstName();
        }

        if (lastBoo) {
            last = "string";
        } else {
            last = req.getLastName();
        }

        if (middleBoo) {
            middle = "string";
        } else {
            middle = req.getMiddleName();
        }
        List<Complains> list = complainRepository.searchByUserName(first, last, middle);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Companiya faollashtirilganligi va o'chirilganligi haqida Companiya Mail ga habar yuborish
    public boolean sendToCompanyEmail(User user) {
        try {
            if (user.getCompany().isActive()) {
                String email = user.getCompany().getContact().getEmail();
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                Date date = new Date();
                mailMessage.setSubject("Sizning Kompaniyangiz  " + date + " da fa`olligi bekor qilindi");
                mailMessage.setText("Ваша компания была деактивирована на " + date
                        + "\n" +
                        "Your company has been deactivated at " + date);
                mailMessage.setTo(email);
                javaMailSender.send(mailMessage);
                return true;
            } else {
                String email = user.getCompany().getContact().getEmail();
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                Date date = new Date();
                mailMessage.setSubject("Sizning Kompaniyangiz  " + date + " da faollashtrildi ");
                mailMessage.setText(
                        "Sizning Kompaniyangiz  " + date + " da faollashtrildi "
                                + "\n" +
                                "Ваша компания была активирована на " + date
                                + "\n" +
                                " Your company has been activated at " + date);
                mailMessage.setTo(email);
                javaMailSender.send(mailMessage);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}