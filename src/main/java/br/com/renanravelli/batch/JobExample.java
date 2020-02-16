package br.com.renanravelli.batch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author renanravelli
 */
@Configuration
@RequiredArgsConstructor
public class JobExample extends JobExecutionListenerSupport {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    @SneakyThrows
    public ItemReader<String> itemReader(@Value("#{jobParameters['file']}") String file) {
        InputStream in = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader::readLine;
    }

    @Bean
    public Step stepExample(@Qualifier("itemReader") ItemReader<String> itemReader) {
        return this.stepBuilderFactory
                .get("STEP_EXAMPLE")
                .chunk(2)
                .reader(itemReader)
                .writer(System.out::println)
                .build();
    }

    @Bean
    public Job job(@Qualifier("stepExample") Step stepExample) {
        return this.jobBuilderFactory
                .get("JOB_EXAMPLE")
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(stepExample)
                .build();
    }
}
