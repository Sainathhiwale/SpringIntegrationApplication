package com.examen.springintegrationapplication;

import com.examen.springintegrationapplication.services.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.inbound.FileReadingMessageSource;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;

@Configuration
@EnableIntegration
public class BankingConfig {

    @Bean
    @InboundChannelAdapter(value = "fileInput", poller = @Poller(fixedDelay = "1000"))
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("filesource"));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "fileInput")
    public MessageHandler transactionHandler(TransactionService transactionService) {
        return message -> {
            File file = (File) message.getPayload();
            try {
                transactionService.processFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}