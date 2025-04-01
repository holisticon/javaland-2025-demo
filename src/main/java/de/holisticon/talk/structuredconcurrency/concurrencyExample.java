import de.holisticon.talk.structuredconcurrency.OrderProcessingTask;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

public void main() {

    final var startTimestampMillis = System.currentTimeMillis();

    // Create order processing tasks
    final Callable<String> order1 = new OrderProcessingTask("Product A", 2_000,
            startTimestampMillis);
    final Callable<String> order2 = new OrderProcessingTask("Product B", 5_000,
            startTimestampMillis);
    final Callable<String> order3 = new OrderProcessingTask("Product C", 10_000,
            startTimestampMillis);
    System.out.println();

    try (
            final var executor = Executors.newVirtualThreadPerTaskExecutor();
            final var taskScope = new StructuredTaskScope.ShutdownOnFailure()
    ) {

        // Submit tasks to the executor
        final var subTask1 = taskScope.fork(() -> executor.submit(order1).get());
        final var subTask2 = taskScope.fork(() -> executor.submit(order2).get());
        final var subTask3 = taskScope.fork(() -> executor.submit(order3).get());

        taskScope.join();

        System.out.println();
        System.out.println(STR."Order for \{subTask1.get()} has arrived in \{System.currentTimeMillis() - startTimestampMillis} milliseconds");
        System.out.println(STR."Order for \{subTask2.get()} has arrived in \{System.currentTimeMillis() - startTimestampMillis} milliseconds");
        System.out.println(STR."Order for \{subTask3.get()} has arrived in \{System.currentTimeMillis() - startTimestampMillis} milliseconds");


    } catch (InterruptedException _) {
    }
}
