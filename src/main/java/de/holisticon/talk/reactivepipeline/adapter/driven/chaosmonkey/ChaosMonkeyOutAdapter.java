package de.holisticon.talk.reactivepipeline.adapter.driven.chaosmonkey;

import de.holisticon.talk.reactivepipeline.application.port.driving.ChaosMonkeyOutPort;
import de.holisticon.talk.reactivepipeline.domain.NumberSevenException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChaosMonkeyOutAdapter implements ChaosMonkeyOutPort {

    @Override
    public Mono<Pair<Integer, String>> causeProblem(@NonNull Pair<Integer, String> pair) {
        final var divisible = pair.getLeft() % 7;
        if (divisible == 0) {
            return Mono.error(new NumberSevenException("I don't like divisible by 7", pair));
        }
        return Mono.just(pair);
    }
}
