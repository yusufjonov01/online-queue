package uz.ecma.queueserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.ecma.queueserver.payload.ReqUserNames;
import uz.ecma.queueserver.service.SupervisorService;

@Component
@EnableScheduling
public class
AutoSupervisorRobot {
    @Autowired
    SupervisorService supervisorService;

    @Scheduled(fixedDelay = 1000 * 60 * 10)
    public void autoScanLetters() {
        supervisorService.companySupervisor();
    }

}
