package log4shell.mitigation.tester;

import java.util.Arrays;
import org.apache.logging.log4j.core.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {
        boolean noLookups = Constants.FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS;
        LOG.info("noLookups {}", noLookups);
        if (noLookups) {
            LOG.info("Lookups are disabled. Example lookup USER=${env:USER}");
        } else {
            LOG.info(
                    "Lookups are enabled! The application is vulnerable for Log4Shell! Example lookup USER=${env:USER}");
        }
        LOG.info("Provided command line arguments are {}", Arrays.asList(args));
        if (args.length > 0) {
            LOG.info(
                    "Since arguments were provided, it's expected to be used for testing Log4Shell. Press CTRL-C to terminate.");
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
