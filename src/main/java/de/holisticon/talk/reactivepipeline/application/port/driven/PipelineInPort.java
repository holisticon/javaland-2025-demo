package de.holisticon.talk.reactivepipeline.application.port.driven;

import reactor.core.publisher.Flux;

public interface PipelineInPort {
    Flux<String> getStreamOfWords();
}
