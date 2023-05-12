# Shell Script Invoker

This Java application uses Apache Camel to invoke a shell script on a timed interval, and logs the output.

## Overview

The application is comprised of a Camel route defined in `ShellInvokerRoute.java`. This route executes once upon startup, then invokes a shell script defined by the `shell.script.path` property in the `application.properties` file.

The output of the shell script is captured and converted into a JSON string by the `ShellScriptProcessor`, which is then logged in the console.

## Code Structure

```java
package com.sample.route;

import com.sample.bean.ShellScriptProcessor;
import org.apache.camel.builder.RouteBuilder;

public class ShellInvokerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer://foo?repeatCount=1")
                .setHeader("scriptPath").simple("{{shell.script.path}}")
                .process(new ShellScriptProcessor())
                .log("Result: ${body}")
                ;
    }
}
```

# Shell Script Processor

This Java application uses Apache Camel and Google Gson to execute a shell script and process its output. The output of the script is logged in JSON format.

## Overview

The `ShellScriptProcessor` class implements Apache Camel's `Processor` interface and is used in a Camel route. It executes a shell script and captures its output and exit value, which it then converts into a JSON string using Google Gson.

## Code Structure

```java
package com.sample.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ShellScriptProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String scriptPath = exchange.getIn().getHeader("scriptPath", String.class);

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(scriptPath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String result = builder.toString();
        int exitValue = process.waitFor();

        Map<String, Object> map = new HashMap<>();
        map.put("exitValue", exitValue);
        map.put("output", result);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        exchange.getIn().setBody(json);
    }
}
```

## How to Use

1. First, ensure you have a `application.properties` file in your classpath (typically in the `src/main/resources` directory). This file should contain a property named `shell.script.path`, pointing to the shell script you wish to execute. For example:

```
shell.script.path=/path/to/your/shellscript.sh
```

2. Build and run the application. The shell script will be invoked once upon startup.

3. The output of the shell script, along with its exit value, will be logged in the console in a JSON format. For example:

```json
{
  "exitValue": 0,
  "output": "Hello, world!\n"
}
```

4. If you wish to change the shell script or its execution frequency, simply update the `shell.script.path` property or the `timer` endpoint URI in `ShellInvokerRoute.java`, respectively.
5. 

## Dependencies

- Apache Camel
- Gson

This project uses Apache Camel for routing and integration, and Gson for JSON serialization. Make sure these dependencies are included in your `pom.xml` file.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/shell-prorgram-invoker-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Camel Core ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-core)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Simple and Tokenize
