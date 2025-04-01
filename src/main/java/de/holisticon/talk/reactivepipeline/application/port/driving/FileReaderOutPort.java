package de.holisticon.talk.reactivepipeline.application.port.driving;

import reactor.core.publisher.Flux;

public interface FileReaderOutPort {
    Flux<String> getAllWordsStream();
}
