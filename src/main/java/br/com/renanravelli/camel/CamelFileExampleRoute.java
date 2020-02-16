package br.com.renanravelli.camel;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author renanravelli
 */
@Component
@RequiredArgsConstructor
public class CamelFileExampleRoute extends RouteBuilder {

    private final CamelFileExampleProcessor processor;

    @Override
    public void configure() throws Exception {
//        when having a file in the directory, the process is executed.
        from("file:///tmp/test")
                .process(processor)
                .to("file:///tmp/test/success");
    }
}
