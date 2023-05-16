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
        String arg1 = exchange.getIn().getHeader("arg1", String.class);
        String arg2 = exchange.getIn().getHeader("arg2", String.class);
        String[] command = {scriptPath, arg1, arg2};

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);

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
