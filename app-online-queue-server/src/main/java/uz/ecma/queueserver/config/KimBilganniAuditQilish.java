package uz.ecma.queueserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.ecma.queueserver.entity.User;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing

public class KimBilganniAuditQilish {
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new KimYozganiniBilish();
    }
}

class KimYozganiniBilish implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal()))) {
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getId());
        }
        return Optional.empty();
    }
}
