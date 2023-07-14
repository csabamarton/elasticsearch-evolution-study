package com.example.elastic.poc;

import com.example.elastic.poc.migration.UserEvolution;
import com.senacor.elasticsearch.evolution.core.ElasticsearchEvolution;
import com.senacor.elasticsearch.evolution.core.api.config.ElasticsearchEvolutionConfig;
import com.senacor.elasticsearch.evolution.spring.boot.starter.autoconfigure.ElasticsearchEvolutionInitializer;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.List;

@SpringBootApplication
public class ElasticApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticApplication.class, args);
    }

    /*@Bean
    public RestClient elasticsearchClient() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
        return restClient;
    }

    @Bean
    public ElasticsearchEvolutionInitializer elasticsearchEvolutionInitializer(ElasticsearchEvolution elasticsearchEvolution) {
        return new ElasticsearchEvolutionInitializer(elasticsearchEvolution);
    }

    @Bean
    public ElasticsearchEvolution elasticsearchEvolution(ElasticsearchEvolutionConfig elasticsearchEvolutionConfig, RestClient elasticsearchClient) {
        return new ElasticsearchEvolution(elasticsearchEvolutionConfig, elasticsearchClient);
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }

    @Bean
    public ElasticsearchEvolutionConfig elasticsearchEvolutionConfig() {
        return ElasticsearchEvolution.configure()
                .setLocations(List.of("classpath:es/migration"))
                .setPlaceholderReplacement(true)
                .setHistoryIndex("es_evolution");
    }

    @Bean
    public UserEvolution userEvolution(ElasticsearchEvolution elasticsearchEvolution) {
        return new UserEvolution(elasticsearchEvolution);
    }*/
}
