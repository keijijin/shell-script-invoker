package com.sample.route;

import com.sample.bean.ShellScriptProcessor;
import org.apache.camel.builder.RouteBuilder;

public class ShellInvokerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer://foo?repeatCount=1")
            .setHeader("scriptPath").simple("{{shell.script.path}}")
            .setHeader("arg1").simple("{{shell.script.arg1}}")
            .setHeader("arg2").simple("{{shell.script.arg2}}")
            .process(new ShellScriptProcessor())
            .log("Result: ${body}")
        ;
    }
}
