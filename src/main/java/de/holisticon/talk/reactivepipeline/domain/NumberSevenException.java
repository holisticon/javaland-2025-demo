package de.holisticon.talk.reactivepipeline.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

public class NumberSevenException extends IllegalArgumentException {

    @Getter
    @Setter
    private Pair<Integer, String> pair;

    public NumberSevenException(String message, Pair<Integer, String> pair) {
        super(message);
        this.pair = pair;
    }
}
