package de.holisticon.talk.reactivepipeline.adapter.driven.ratelimit;

import de.holisticon.talk.reactivepipeline.application.port.driving.RatelimitOutPort;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class RatelimitOutAdapter implements RatelimitOutPort {

    public static final int MAX_ATTEMPTS = 5;
    public static final int RETRY_BACKOFF_MILLIS = 1_000;

    private final @NonNull RateLimiterRegistry rateLimiterRegistry;

    private RateLimiter rateLimiter;

    @PostConstruct
    public void setup() {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("word-stream-limiter");
    }

    @Override
    public Mono<Pair<Integer, String>> applyRateLimit(@NonNull Pair<Integer, String> pair) {
        return Mono.just(pair)
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .retryWhen(getRetrySpec());
    }

    public RetryBackoffSpec getRetrySpec() {
        return Retry.backoff(MAX_ATTEMPTS, Duration.ofMillis(RETRY_BACKOFF_MILLIS))
                .doAfterRetry(retrySignal -> log.warn("rate limit applied"));
    }
}
