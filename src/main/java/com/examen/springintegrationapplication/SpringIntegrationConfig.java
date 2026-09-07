package com.examen.springintegrationapplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.LastModifiedFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.inbound.FileReadingMessageSource;
import org.springframework.integration.file.outbound.FileWritingMessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

    @Bean
    @InboundChannelAdapter(value = "fileInput",poller = @Poller(fixedDelay = "1000"))
    public FileReadingMessageSource fileReadingMessageSource(){
        CompositeFileListFilter<File> filter=new CompositeFileListFilter<>();
        filter.addFilter(new SimplePatternFileListFilter("*.txt"));
        LastModifiedFileListFilter modifiedFileListFilter=new LastModifiedFileListFilter();
        modifiedFileListFilter.setAge(60);
        FileReadingMessageSource source=new FileReadingMessageSource();
        source.setDirectory(new File("filesource"));
        source.setFilter(filter);
        source.setFilter(modifiedFileListFilter);
        return source;
    }
    @Bean
    @ServiceActivator(inputChannel = "fileInput")
    public FileWritingMessageHandler handler(){
        FileWritingMessageHandler handler=new FileWritingMessageHandler(new File("destination"));
        handler.setAutoCreateDirectory(true);
        handler.setExpectReply(false);
        return handler;
    }
}