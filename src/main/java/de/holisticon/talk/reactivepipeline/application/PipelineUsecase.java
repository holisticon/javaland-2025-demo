package de.holisticon.talk.reactivepipeline.application;

import de.holisticon.talk.reactivepipeline.application.port.driven.PipelineInPort;
import de.holisticon.talk.reactivepipeline.application.port.driving.ChaosMonkeyOutPort;
import de.holisticon.talk.reactivepipeline.application.port.driving.FileReaderOutPort;
import de.holisticon.talk.reactivepipeline.application.port.driving.RatelimitOutPort;
import de.holisticon.talk.reactivepipeline.domain.NumberSevenException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineUsecase implements PipelineInPort {

    private final @NonNull FileReaderOutPort fileReaderOutPort;
    private final @NonNull RatelimitOutPort ratelimitOutPort;
    private final @NonNull ChaosMonkeyOutPort chaosMonkeyOutPort;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Flux<String> getStreamOfWords() {

        return fileReaderOutPort.getAllWordsStream()

                .map(word -> Pair.of(counter.addAndGet(1), word))
                .flatMap(ratelimitOutPort::applyRateLimit)
                .flatMap(pair -> chaosMonkeyOutPort.causeProblem(pair)
                        .onErrorResume(throwable -> throwable instanceof NumberSevenException, resumeOnError())
                        .onErrorContinue(NumberSevenException.class, skipFaultyElement())
                )
                .bufferTimeout(12, Duration.ofSeconds(10))
                .map(wordList -> {
                    log.info("{} {}", wordList.size(), wordList.stream().map(Pair::getRight).toList());
                    return wordList;
                })
                .flatMapIterable(Function.identity())
                .doOnNext(pair -> log.info("{} {}", pair.getLeft(), pair.getRight()))
                .map(Pair::getRight);
    }


    BiConsumer<Throwable, Object> skipFaultyElement() {
        return (t, o) -> log.error("ERROR {} {}", t, o);
    }

    Function<Throwable, Mono<? extends Pair<Integer, String>>> resumeOnError() {
        return error -> {
            if (error instanceof NumberSevenException e) {
                log.error("ERROR {} {}", error.getMessage(), e.getPair());
                return Mono.just(e.getPair());
            }
            return Mono.just(Pair.of(-1, "default value"));
        };
    }
}
