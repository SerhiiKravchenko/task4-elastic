package com.epam.learn.elastic.task4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@Configuration
public class ElasticConfig {

    private String serverUrl = "http://localhost:9200";
    private String apiKey = "NFpxQThwWUIwdnpPaGFJT0tHRkE6blpQQ2laNE00OFNaX0sxSDUyZzhhZw==";

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        return ElasticsearchClient.of(b -> b
                .host(serverUrl)
                .apiKey(apiKey));
    }
}
