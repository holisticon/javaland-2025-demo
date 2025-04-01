package de.holisticon.talk.reactivepipeline.application.port.driving;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Mono;

public interface ChaosMonkeyOutPort {
    Mono<Pair<Integer, String>> causeProblem(@NonNull Pair<Integer, String> pair);
}
