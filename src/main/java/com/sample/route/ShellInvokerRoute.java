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
