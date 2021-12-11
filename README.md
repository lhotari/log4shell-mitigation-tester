# Log4Shell Mitigation tester

This is an example application using Log4j 2.14.1 .

source code: [App.java](app/src/main/java/log4shell/mitigation/tester/App.java)

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

Test some message lookup feature by passing a string on command line:
```
FOO='Hello ${env:USER}' java -jar app/build/libs/app-all.jar '${env:FOO}'
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

## Seeing is believing - Debug it your self in an IDE

(pending, I'm currently adding changes to the app to be able to test this)

You can also debug the solution and place a break point in the vulnerable code which is 
https://github.com/apache/logging-log4j2/blob/dd18e9b21009055e226daf5b233c92b6a17934ca/log4j-core/src/main/java/org/apache/logging/log4j/core/pattern/MessagePatternConverter.java#L119-L135

Set the debugger in class is `org.apache.logging.log4j.core.pattern.MessagePatternConverter` at line 128.

When the mitigation is in place, the debugger should never get in the code block.


## Kubernetes / docker mitigation solutions for Log4Shell

It is necessary to mitigate Log4Shell immediately without waiting for a new software release. Here are some solutions for doing that quickly and effectively.

### Patching existing docker images with a thin overlay that sets LOG4J_FORMAT_MSG_NO_LOOKUPS=true env

This is a generic solution:
https://github.com/lhotari/Log4Shell-mitigation-Dockerfile-overlay

### Example of patching existing docker images with Log4j 2.15.0 jar files

This is not generic, example is from apache/pulsar:
https://github.com/lhotari/pulsar-docker-images-patch-CVE-2021-44228

### Patching k8s deployments with LOG4J_FORMAT_MSG_NO_LOOKUPS=true env

https://gist.github.com/brunoborges/9df576689b404aee70a8065210c77fb3


