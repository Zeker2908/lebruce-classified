package ru.lebruce.store.domain.component;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.lebruce.store.domain.model.ConfirmationToken;
import ru.lebruce.store.repository.ConfirmationTokenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Scheduled(fixedRate = 300000) //каждые 5 минут
    public void cleanUpExpiredTokens() {
        List<ConfirmationToken> expiredTokens = confirmationTokenRepository
                .findAllByExpiresAtBefore(LocalDateTime.now());
        confirmationTokenRepository.deleteAll(expiredTokens);
    }
}
