package ru.javaops.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class TimingRules {
    // https://dzone.com/articles/applying-new-jdk-11-string-methods
    private static final String DELIM = "-".repeat(103);

    private static final Logger log = getLogger("result");
    private static StringBuilder results = new StringBuilder();

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public static Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            var result = format("%-95s %7d", description.getMethodName(), NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void before() {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info('\n' + DELIM
                    + "\nTest" + " ".repeat(86) + "Durations, ms"
                    + '\n' + DELIM + '\n' + results + DELIM + '\n'
            );
        }
    };

}