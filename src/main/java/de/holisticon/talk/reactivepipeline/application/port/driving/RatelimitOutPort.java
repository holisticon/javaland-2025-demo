package de.holisticon.talk.reactivepipeline.application.port.driving;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Mono;

public interface RatelimitOutPort {
    Mono<Pair<Integer, String>> applyRateLimit(@NonNull Pair<Integer, String> pair);
}
