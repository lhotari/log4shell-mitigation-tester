# Log4Shell Mitigation tester

This is an example application using Log4j 2.14.1 .

source code: [App.java](app/src/main/java/log4shell/mitigation/tester/App.java)

The purpose of this is to be able to test different mitigation approaches. The mitigation approaches are also mentioned in [Microsoft’s Response to CVE-2021-44228 Apache Log4j 2](https://msrc-blog.microsoft.com/2021/12/11/microsofts-response-to-cve-2021-44228-apache-log4j2/).

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

Test ``LOG4J_FORMAT_MSG_NO_LOOKUPS=true`` environment variable workaround, https://twitter.com/brunoborges/status/1469462412679991300
```
LOG4J_FORMAT_MSG_NO_LOOKUPS=true java -jar app/build/libs/app-all.jar
```

Test `JAVA_TOOL_OPTIONS=-Dlog4j.formatMsgNoLookups=true` environment variable workaround, https://twitter.com/brunoborges/status/1469426918550245377
```
JAVA_TOOL_OPTIONS=-Dlog4j.formatMsgNoLookups=true java -jar app/build/libs/app-all.jar
```

Test log4j2.component.properties in classpath workaround solution:
```
java -cp log4j2-formatMsgNoLookups/build/libs/log4j2-formatMsgNoLookups.jar:app/build/libs/app-all.jar log4shell.mitigation.tester.App
```

## Seeing is believing - exploit this sample app

When you run the app, you will see the vulnerability in action:
```bash
❯ java -jar app/build/libs/app-all.jar '${jndi:ldap://127.0.0.1/a?user=${env:USER}}'
[2021-12-12 10:57:07,216] [main] [log4shell.mitigation.tester.App] INFO noLookups false
[2021-12-12 10:57:07,218] [main] [log4shell.mitigation.tester.App] INFO Lookups are enabled! The application is vulnerable for Log4Shell! Example lookup USER=lari
2021-12-12 10:57:07,239 main WARN Error looking up JNDI resource [ldap://127.0.0.1/a?user=lari]. javax.naming.InvalidNameException: ldap://127.0.0.1/a?user=lari
	at java.naming/com.sun.jndi.url.ldap.ldapURLContext.lookup(ldapURLContext.java:92)
	at java.naming/javax.naming.InitialContext.lookup(InitialContext.java:409)
```

You can also debug the solution and place a break point in the vulnerable code which is 
https://github.com/apache/logging-log4j2/blob/dd18e9b21009055e226daf5b233c92b6a17934ca/log4j-core/src/main/java/org/apache/logging/log4j/core/pattern/MessagePatternConverter.java#L119-L135

Set the debugger in class is `org.apache.logging.log4j.core.pattern.MessagePatternConverter` at line 128.

When the mitigation is in place, the debugger should never get in the code block. The LDAP call shouldn't get attempted either:

```bash
❯ LOG4J_FORMAT_MSG_NO_LOOKUPS=true java -jar app/build/libs/app-all.jar '${jndi:ldap://127.0.0.1/a?user=${env:USER}}'
[2021-12-12 10:59:15,589] [main] [log4shell.mitigation.tester.App] INFO noLookups true
[2021-12-12 10:59:15,590] [main] [log4shell.mitigation.tester.App] INFO Lookups are disabled. Example lookup USER=${env:USER}
[2021-12-12 10:59:15,591] [main] [log4shell.mitigation.tester.App] INFO Provided command line arguments are [${jndi:ldap://127.0.0.1/a?user=${env:USER}}]
```

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


