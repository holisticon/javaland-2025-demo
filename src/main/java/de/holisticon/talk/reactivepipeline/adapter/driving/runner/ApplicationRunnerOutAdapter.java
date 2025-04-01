package de.holisticon.talk.reactivepipeline.adapter.driving.runner;

import de.holisticon.talk.reactivepipeline.application.port.driven.PipelineInPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationRunnerOutAdapter implements ApplicationRunner {

    private final @NonNull PipelineInPort pipelineInPort;

    @Override
    public void run(final ApplicationArguments args) {
        log.info("ApplicationRunner started");
        pipelineInPort.getStreamOfWords()
                .doOnComplete(()-> log.info("ApplicationRunner completed"))
                .subscribe();
    }
}
