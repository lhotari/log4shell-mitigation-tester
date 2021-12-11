package log4shell.mitigation.tester;

import org.apache.logging.log4j.core.util.Constants;

public class App {
    public static void main(String[] args) {
        boolean noLookups = Constants.FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS;
        System.out.println("noLookups " + noLookups);
        if (noLookups) {
            System.out.println("Lookups are disabled.");
        } else {
            System.out.println("Lookups are enabled! The application is vulnerable for Log4Shell!");
        }
    }
}
