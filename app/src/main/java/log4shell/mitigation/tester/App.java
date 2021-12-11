package log4shell.mitigation.tester;

import org.apache.logging.log4j.core.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        boolean noLookups = Constants.FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS;
        LOG.info("noLookups {}", noLookups);
        if (noLookups) {
            LOG.info("Lookups are disabled. Example lookup USER=${env:USER}");
        } else {
            LOG.info("Lookups are enabled! The application is vulnerable for Log4Shell! Example lookup USER=${env:USER}");
        }
    }
}
