package uz.ecma.queueserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecma.queueserver.entity.*;
import uz.ecma.queueserver.entity.Queue;
import uz.ecma.queueserver.entity.enums.QueueStatus;
import uz.ecma.queueserver.entity.enums.RoleName;
import uz.ecma.queueserver.exception.BadRequestException;
import uz.ecma.queueserver.payload.ApiResponse;
import uz.ecma.queueserver.payload.ReqQueue;
import uz.ecma.queueserver.payload.ResPageable;
import uz.ecma.queueserver.payload.ResQueue;
import uz.ecma.queueserver.repository.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class QueueService {
    @Autowired
    QueueRepository queueRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DirectionRepository directionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OperatorDirectionRepository operatorDirectionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CheckRole checkRole;

    public ApiResponse addQueue(ReqQueue request, User user) {

        if (!userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            User newUser = new User();
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setRoles(roleRepository.findAllByRoleNameIn(new ArrayList<>(Collections.singletonList(RoleName.USER))));
            newUser.setPassword(passwordEncoder.encode("123"));
            userRepository.save(newUser);
            return getQueue(request, newUser);
        } else {
            return getQueue(request, user);
        }

    }

    public ApiResponse getQueue(ReqQueue request, User user) {
        if (user == null) {
            user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("getUser"));
        }
        if (isTrue(RoleName.USER, user) || isTrue(RoleName.RECEPTION, user)) {
        } else {
            return new ApiResponse(
                    "Sizda bunday huquq yo'q",
                    "",
                    "",
                    "",
                    false
            );
        }
        Direction direction = directionRepository.findById(request.getDirectionId()).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        boolean isHave = (isDisposable(direction) ?
                queueRepository.existsByClient_idAndDirection_id(user.getId(), direction.getId()) :
                (queueRepository.existsByDirectionIdAndClientIdAndDateType(request.getDirectionId(), user.getId(),
                        (isDirectionType(direction))).isPresent()));
        if (isHave) {
            return new ApiResponse(false, "Siz allaqachon navbat olib bo'lgansiz!");
        }
        Queue newQueue = new Queue();
        newQueue.setStartTime(new Timestamp(new Date().getTime()));
        newQueue.setStatus(QueueStatus.WAITING);
        newQueue.setClient(user);
        newQueue.setDirection(direction);
        Integer countQueueNumber = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getCountAllQueue(request.getDirectionId())) :
                queueRepository.getCountAllQueueByDirectionAndDateNameAndTimestamp
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day")
                        );
        newQueue.setQueueNumber((countQueueNumber > 0 ? countQueueNumber : 0) + 1);

        Queue getQueue = queueRepository.save(newQueue);
        return new ApiResponse(
                "Navbat muvaffaqiyatli olindi",
                "Queued successfully",
                "В очереди успешно",
                "Навбат муваффакиятли олинди",
                true,
                new ResQueue(
                        getQueue.getId(),
                        getQueue.getClient().getPhoneNumber(),
                        getDurationTime(request.getDirectionId()),
                        queueRepository.getCountOperator(request.getDirectionId()),
                        getQueue.getDirection().getId(),
                        null,
                        getQueue.getStatus().toString(),
                        getQueue.getQueueNumber(),
                        getProbablyTime(request.getDirectionId()),
                        null,
                        null
                ));

    }

    public ApiResponse isPhoneNumberHave(String phoneNumber, Integer directionId) {
        Optional<User> usersByPhoneNumber = userRepository.findUsersByPhoneNumber(phoneNumber.substring(1));
        if (usersByPhoneNumber.isPresent()) {
            User user = usersByPhoneNumber.orElseThrow(() -> new ResourceNotFoundException("getUser"));
            if (checkRole.isUser(user)) {
                Direction direction = directionRepository.findById(directionId).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
                boolean isHave = isDisposable(direction) ?
                        queueRepository.existsByClient_idAndDirection_id(user.getId(), direction.getId()) :
                        queueRepository.existsByDirectionIdAndClientIdAndDateType(directionId, user.getId(),
                                isDirectionType(direction)).isPresent();
                if (isHave) {
                    return new ApiResponse(false, "Siz allaqachon navbat olib bo'lgansiz!");
                } else {
                    return new ApiResponse(true);
                }
            } else {
                return new ApiResponse(false);
            }
        }
        return new ApiResponse(true);
    }

    public ApiResponse getQueueAccepted(String statusType, User user) {
        if (isTrue(RoleName.OPERATOR, user)) {
            OperatorDirection direction = operatorDirectionRepository.getDirectionId(user.getId());
            Integer directionId = direction.getDirection().getId();
            Optional<Queue> byDirection_idAndStatus = queueRepository.findByDirection_IdAndStatusAndOperator_Id(directionId, QueueStatus.valueOf(statusType.toUpperCase()), user.getId());
            Queue queue = byDirection_idAndStatus.get();
            return new ApiResponse(true, queue);
        }
        return null;
    }

    public ResQueue getQueue(User user) {
        if (isTrue(RoleName.OPERATOR, user)) {
            OperatorDirection direction = operatorDirectionRepository.getDirectionId(user.getId());
            Integer directionId = direction.getDirection().getId();
            Integer waiting = queueRepository.getCountQueue(directionId, "WAITING");
            String durationTime = getDurationTime(directionId);
            Integer waiting1 = (queueRepository.getLastQueue(directionId, "WAITING")).getQueueNumber();
            return new ResQueue(
                    directionId,
                    waiting,
                    durationTime,
                    waiting1
            );
        }
        return new ResQueue();
    }

    public ResQueue getQueueForClient(Integer id) {
        Direction direction = directionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        Integer allWaitingQueue = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getCountQueue(id, "WAITING")) :
                queueRepository.getCountAllQueueByDirectionAndDateNameAndTimestampAndStatus
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "WAITING"
                        );
        Integer queueMaxAcceptedNumber = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getQueueMaxAcceptedNumber(id)) :
                queueRepository.getMaxQueueNumberByDirectionAndDateNameAndTimestampAndStatus
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "ACCEPTED"
                        );
        Integer queueMinWaitingNumber = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getQueueMaxAcceptedNumber(id)) :
                queueRepository.getMinQueueNumberByDirectionAndDateNameAndTimestampAndStatus
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "WAITING"
                        );
        Integer queueLastWaitingNumber = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getQueueMaxAcceptedNumber(id)) :
                queueRepository.getMaxQueueNumberByDirectionAndDateNameAndTimestampAndStatus
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "WAITING"
                        );
        String durationTime = getDurationTime(id);
        String probablyTime = getProbablyTime(id);
        return new ResQueue(
                id,
                queueMaxAcceptedNumber == null ? queueMinWaitingNumber : queueMaxAcceptedNumber,
                allWaitingQueue,
                durationTime,
                queueLastWaitingNumber,
                probablyTime
        );
    }

    public ApiResponse getClientQueue(Integer id, User user) {
        if (user == null) {
            return new ApiResponse(false);
        }
        if (isTrue(RoleName.USER, user)) {
            Direction direction = directionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
            Optional<Queue> optionalQueue = isDisposable(direction) ?
                    queueRepository.findByDirection_IdAndClient_Id(direction.getId(), user.getId()) :
                    queueRepository.findByDirectionIdAndClientIdAndDateType(direction.getId(), user.getId(), isDirectionType(direction));
            String clientQueueTime = "";
            if (optionalQueue.isPresent()) {
                Queue queue = optionalQueue.get();
                if (queue.getStatus().name().equals("WAITING")) {
                    clientQueueTime = getClientQueueTime(id, queue);
                }
                return new ApiResponse(true, optionalQueue.get(), clientQueueTime);
            }
            return new ApiResponse(false, optionalQueue.get(), "0");
        }
        return new ApiResponse(false, "Bunday client yo'q");
    }

    public String getClientQueueTime(Integer id, Queue queue) {
        Integer queueMaxAcceptedNumber = queueRepository.getQueueMaxAcceptedNumber(id);
        Integer queueMinWaitingNumber = queueRepository.getQueueMinWaitingNumber(id);
        Integer durationTime = getTime(id);
        if ((queueMaxAcceptedNumber != null || queueMinWaitingNumber != null) && durationTime > 0) {
            int countQueue = 0;
            if (queueMaxAcceptedNumber != null) {
                countQueue = queue.getQueueNumber() - queueMaxAcceptedNumber;
            } else {
                countQueue = queue.getQueueNumber() - queueMinWaitingNumber;
            }
            int time = countQueue * durationTime;
            if (time > 0) {
                if (time > 60) {
                    int hours = time / 60;
                    int min = time % 60;
                    if (hours > 24) {
                        int day = hours / 24;
                        hours = hours - (day * 24);
                        return day + " kun " + hours + " soat " + min + " min";
                    }
                    return hours + " soat " + min + " min";
                }
                return time + " min";
            }
        }
        return " 00:00";
    }

    public ResPageable getQueuesByDirection(Integer directionId, Integer size, String status) {

        switch (status.toUpperCase()) {
            case "WAITING":
                return getPageable("WAITING", size, directionId);
            case "DELAY":
                return getPageable("DELAY", size, directionId);
            case "CLOSED":
                return getPageable("CLOSED", size, directionId);
            case "REJECT":
                return getPageable("REJECT", size, directionId);
            case "CANCELED":
                return getPageable("CANCELED", size, directionId);
            default:
                return new ResPageable();
        }
    }

    public ApiResponse getQueueList(String status, Integer page, Integer size, User user) {
        try {
            if (page < 0) {
                throw new BadRequestException("Page 0 dan kichik bo'lishi mumkin emas");
            }
            if (size < 1) {
                throw new BadRequestException("Size 1 dan kichik bo'lishi mumkin emas");
            }
            Integer directionId = null;
            if (checkRole.isOperator(user)) {
                OperatorDirection directionId1 = operatorDirectionRepository.getDirectionId(user.getId());
                directionId = directionId1.getDirection().getId();
            }
            Pageable pageable = PageRequest.of(page, size);
            UUID companyId = userRepository.findByPhoneNumber(user.getPhoneNumber()).orElseThrow(() -> new ResourceNotFoundException("getUser")).getCompany().getId();
            Page<Queue> queuePage = (
                    status.equals("ALL") ?
                            (directionId != null ?
                                    queueRepository.findAllByDirectionAndOperatorId(directionId, pageable, user.getId())
                                    : queueRepository.findAllCompanyId(companyId, pageable))
                            : (directionId != null ?
                            status.equals("WAITING") ?
                                    queueRepository.findAllByDirection(directionId, status, pageable)
                                    : queueRepository.findAllByDirectionAndOperatorIdAndStatus(directionId, status, pageable, user.getId())
                            : queueRepository.findAllByStatusAndCompanyId(status, companyId, pageable))
            );
            return new ApiResponse(
                    "Mana Yangi Kampaniyalar",
                    "Here are the new campaigns",
                    "Вот новые кампании",
                    "Мана Янги Кампаниялар",
                    true,
                    new ResPageable(
                            page, size, queuePage.getTotalPages(),
                            queuePage.getTotalElements(),
                            queuePage.getContent()
                    )
            );
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }

    public ResPageable getPageable(String status, Integer size, Integer directionId) {
        ResPageable resPageable = new ResPageable();
        Direction direction = directionRepository.findById(directionId).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        Page<Queue> queue = direction.getDirectionDateName().name().equals("DISPOSABLE") ?
                queueRepository.findAllByDirection(directionId, status, PageRequest.of(0, size)) :
                queueRepository.findAllByDirectionAndStatusAndDateType(directionId, status,
                        (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                        PageRequest.of(0, size));
        resPageable.setPage(0);
        resPageable.setSize(size);
        resPageable.setTotalElements(queue.getTotalElements());
        resPageable.setTotalPages(queue.getTotalPages());
        List<ResQueue> resQueues = new ArrayList<>();
        List<Queue> content = queue.getContent();
        for (Queue oneQueue : content) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMM, HH:mm");
            resQueues.add(
                    new ResQueue(
                            oneQueue.getId(),
                            oneQueue.getClient().getPhoneNumber(),
                            getDurationTime(directionId),
                            queueRepository.getCountOperator(directionId),
                            oneQueue.getDirection().getId(),
                            oneQueue.getOperator() == null ? null : oneQueue.getOperator().getId(),
                            status,
                            oneQueue.getQueueNumber(),
                            getProbablyTime(directionId),
                            simpleDateFormat.format(oneQueue.getStartTime()),
                            simpleDateFormat.format((oneQueue.getFinishTime()) == null ? new Date() : oneQueue.getFinishTime())
                    ));
        }
        resPageable.setObject(resQueues);
        return resPageable;
    }

    public String getDurationTime(Integer directionId) {
        Integer time = getTime(directionId);
        if (time > 60) {
            int hours = time / 60;
            int min = time % 60;

            if (hours > 24) {
                int day = hours / 24;
                hours = hours - (day * 24);
                return day + " kun " + hours + " soat " + min + " min";
            }
            return hours + " soat " + min + " min";
        }
        return time + " min";
    }

    public ApiResponse setStatus(UUID queueId, String status, User user) {

        if (isTrue(RoleName.OPERATOR, user)) {
            Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new ResourceNotFoundException("getQueue"));
            switch (status.toUpperCase()) {
                case "DELAY":
                    if (queue.getStatus().equals(QueueStatus.WAITING)) {
                        queue.setStartTime(new Timestamp(new Date().getTime()));
                        queue.setStatus(QueueStatus.DELAY);
                        queue.setOperator(user);
                        queueRepository.save(queue);
                        return
                                new ApiResponse(
                                        "Navbat kechiktirildi",
                                        "Delay the queue",
                                        "Задержать очередь",
                                        "Навбат кечиктирилди",
                                        true
                                );
                    } else {
                        return
                                new ApiResponse(
                                        "Xatolik",
                                        "Error",
                                        "Ошибка",
                                        "Хатолик",
                                        false
                                );
                    }
                case "ACCEPTED":
                    if (queue.getStatus().equals(QueueStatus.WAITING) || queue.getStatus().equals(QueueStatus.DELAY)) {
                        queue.setStatus(QueueStatus.ACCEPTED);
                        queue.setStartTime(new Timestamp(new Date().getTime()));
                        queue.setOperator(user);
                        queueRepository.save(queue);
                        return
                                new ApiResponse(
                                        "Navbat tasdiqlandi",
                                        "Confirm Queue",
                                        "Подтвердить очередь",
                                        "Навбат тасдикланди",
                                        true
                                );
                    } else {
                        return
                                new ApiResponse(
                                        "Xatolik",
                                        "Error",
                                        "Ошибка",
                                        "Хатолик",
                                        false
                                );
                    }
                case "CLOSED":
                    if (queue.getStatus().equals(QueueStatus.ACCEPTED)) {
                        queue.setStatus(QueueStatus.CLOSED);
                        queue.setFinishTime(new Timestamp(new Date().getTime()));
                        queue.setOperator(user);
                        queueRepository.save(queue);
                        return
                                new ApiResponse(
                                        "Navbat yakunlandi",
                                        "Complete the queue",
                                        "Завершить очередь",
                                        "Навбат якунланди",
                                        true
                                );
                    } else {
                        return
                                new ApiResponse(
                                        "Xatolik",
                                        "Error",
                                        "Ошибка",
                                        "Хатолик",
                                        false
                                );
                    }
                default:
                    return
                            new ApiResponse(
                                    "Xatolik",
                                    "Error",
                                    "Ошибка",
                                    "Хатолик",
                                    false
                            );
            }
        }
        return new ApiResponse();
    }

    public String getProbablyTime(Integer directionId) {
        Direction direction = directionRepository.findById(directionId).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        Integer waiting = direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                (queueRepository.getCountQueue(directionId, "WAITING")) :
                queueRepository.getCountAllQueueByDirectionAndDateNameAndTimestampAndStatus
                        (direction.getId(),
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "WAITING"
                        );
        Integer time = getTime(directionId);

        int probablyTime = (waiting - 1) * time;
        if (probablyTime >= 60) {
            int hours = probablyTime / 60;
            int min = probablyTime % 60;

            if (hours > 24) {
                int day = hours / 24;
                hours = hours - (day * 24);
                return day + " kun " + hours + " soat " + min + " min";
            }
            return hours + " soat " + min + " min";
        }
        return probablyTime + " min";
    }

    public Integer getTime(Integer directionId) {
        Direction direction = directionRepository.findById(directionId).orElseThrow(() -> new ResourceNotFoundException("getDirection"));
        List<Queue> queues = (direction.getDirectionDateName().toString().equals("DISPOSABLE") ?
                queueRepository.findAllByDirectionLimit(directionId, "CLOSED") :
                (queueRepository
                        .getAllQueueByDirectionAndDateNameAndTimestampAndStatus(
                                directionId,
                                (direction.getDirectionDateName().name().equals("WEEKLY") ? "week" : "day"),
                                "CLOSED")));
        int count = 0;
        int sum = 0;
        if (queues.isEmpty()) {
            return 0;
        }
        for (Queue queue : queues) {
            Integer hours = calculateTime(queue, "HH");
            if (hours > 0) {
                Integer min = calculateTime(queue, "mm");
                sum = sum + min + (hours * 60);
                count++;
            }
            Integer min = calculateTime(queue, "mm");
            sum = sum + min;
            count++;
        }
        return sum / count;
    }

    public Integer calculateTime(Queue queue, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime start = queue.getStartTime().toLocalDateTime();
        int startTime = Integer.parseInt(start.format(formatter));

        LocalDateTime finish = queue.getFinishTime().toLocalDateTime();
        int finishTime = Integer.parseInt(finish.format(formatter));

        if (finishTime >= startTime) {
            return finishTime - startTime;
        }
        return (60 - startTime) + finishTime;
    }

    public boolean isTrue(RoleName roleName, User user) {
        boolean isTrue = false;
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == roleName) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

    public boolean isDisposable(Direction direction) {
        return direction.getDirectionDateName().name().equals("DISPOSABLE");
    }

    public String isDirectionType(Direction direction) {
        if (direction.getDirectionDateName().name().equals("WEEKLY")) {
            return "week";
        } else {
            return "day";
        }
    }

    @Scheduled(fixedDelay = 1000 * 60 * 3)
    public void toReject() {
        List<Queue> delay = queueRepository.findAllByStatus("DELAY");
        for (Queue queue : delay) {
            Timestamp startTime = queue.getStartTime();
            Date date = new Date(startTime.getTime() + 300000 * 6);

            if (date.getTime() < new Date().getTime()) {
                queue.setStatus(QueueStatus.REJECT);
                queue.setFinishTime(new Timestamp(new Date().getTime()));
                queueRepository.save(queue);
            }
        }
    }
}