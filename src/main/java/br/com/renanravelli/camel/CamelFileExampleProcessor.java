package br.com.renanravelli.camel;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

/**
 * @author renanravelli
 */
@Component
@RequiredArgsConstructor
public class CamelFileExampleProcessor implements Processor {

    private final Job job;
    private final JobLauncher jobLauncher;

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileAbsolutePath = String.valueOf(exchange.getIn().getHeaders().get("CamelFileAbsolutePath"));

        JobParametersBuilder parameters = new JobParametersBuilder();
        parameters.addLong("sys", System.currentTimeMillis());
        parameters.addString("file", fileAbsolutePath);

        this.jobLauncher.run(job, parameters.toJobParameters());
    }
}
