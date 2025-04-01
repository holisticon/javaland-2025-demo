package de.holisticon.talk.reactivepipeline.adapter.driven.filereader;

import de.holisticon.talk.reactivepipeline.application.port.driving.FileReaderOutPort;
import de.holisticon.talk.reactivepipeline.infrastructure.ApplicationProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.BaseStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileReaderOutAdapter implements FileReaderOutPort {

    private final @NonNull ApplicationProperties applicationProperties;

    @Override
    public Flux<String> getAllWordsStream() {
        final var systemResource = ClassLoader.getSystemResource(applicationProperties.getFileName()).getPath();
        final var inputFile = Paths.get(systemResource);
        return Flux.using(() -> Files.lines(inputFile), Flux::fromStream, BaseStream::close)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(line -> Arrays.asList(line.split("\\s+")));
    }
}
