# Log4Shell Mitigation tester

This is an example application using Log4j 2.14.1 .
The purpose of this is to be able to test different mitigation approaches.

## Example usage

Examples are for a bash shell


build the app
```
./gradlew assemble
```

Run without workaround
```
java -jar app/build/libs/app-all.jar
```

Test `-Dlog4j2.formatMsgNoLookups=true` system property workaround, https://twitter.com/brunoborges/status/1469186875608875011
```
java -Dlog4j2.formatMsgNoLookups=true -jar app/build/libs/app-all.jar
```

Test `LOG4J_FORMAT_MSG_NO_LOOKUPS=true` environment variable workaround, https://twitter.com/brunoborges/status/1469462412679991300
```
LOG4J_FORMAT_MSG_NO_LOOKUPS=true java -jar app/build/libs/app-all.jar
```

Test `JAVA_TOOL_OPTIONS=-Dlog4j.formatMsgNoLookups=true` environment variable workaround, https://twitter.com/brunoborges/status/1469426918550245377
```
JAVA_TOOL_OPTIONS=-Dlog4j.formatMsgNoLookups=true java -jar app/build/libs/app-all.jar
```

