package ru.javaops.topjava;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.springframework.util.StopWatch;

import static org.slf4j.LoggerFactory.getLogger;

public class TimingExtension
        implements BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, AfterAllCallback {
    private static final Logger log = getLogger("result");

    private StopWatch stopWatch;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        stopWatch = new StopWatch("Execution time of " + context.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        var testName = context.getDisplayName();
        log.info("\nStart " + testName);
        stopWatch.start(testName);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        stopWatch.stop();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        log.info('\n' + stopWatch.prettyPrint() + '\n');
    }
}