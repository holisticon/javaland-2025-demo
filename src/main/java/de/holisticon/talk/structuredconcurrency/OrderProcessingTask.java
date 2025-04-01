package de.holisticon.talk.structuredconcurrency;

import java.util.concurrent.Callable;

public class OrderProcessingTask implements Callable<String> {

    private final String item;
    private final int processingTimeMillis;
    private final long startTimestampMillis;

    public OrderProcessingTask(final String item, final int processingTimeMillis, final long startTimestampMillis) {
        super();

        this.item = item;
        this.processingTimeMillis = processingTimeMillis;
        this.startTimestampMillis = startTimestampMillis;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(processingTimeMillis); // Simulating processing time
        System.out.println(STR."Order for \{item} was processed in \{System.currentTimeMillis() - startTimestampMillis} milliseconds.");
        return item;
    }
}
