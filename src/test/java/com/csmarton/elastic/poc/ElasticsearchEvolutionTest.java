package com.csmarton.elastic.poc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senacor.elasticsearch.evolution.core.ElasticsearchEvolution;
import com.senacor.elasticsearch.evolution.core.api.config.ElasticsearchEvolutionConfig;
import com.senacor.elasticsearch.evolution.core.api.migration.HistoryRepository;
import com.senacor.elasticsearch.evolution.core.internal.migration.execution.HistoryRepositoryImpl;
import com.senacor.elasticsearch.evolution.core.internal.migration.execution.MigrationScriptProtocolMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ElasticsearchEvolutionTest {

    private RestHighLevelClient restHighLevelClient;

    private HistoryRepository historyRepository;

    private ElasticsearchEvolution elasticsearchEvolution;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String INDEX_NAME = "users-test";
    private static final String HISTORY_INDEX = "es-evolution-test"; // Assuming the history index is named "es-evolution"
    private static final String ELASTICSEARCH_HOST = "localhost";
    private static final int ELASTICSEARCH_PORT = 9200;
    private static final String ELASTICSEARCH_SCHEME = "http";

    @BeforeEach
    void setUp() {
        // Create the Elasticsearch client configuration
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(ELASTICSEARCH_HOST, ELASTICSEARCH_PORT, ELASTICSEARCH_SCHEME)
        );

        // Create the RestHighLevelClient instance
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        ElasticsearchEvolutionConfig elasticsearchEvolutionConfig = ElasticsearchEvolution.configure()
                .setLocations(singletonList("classpath:es/evolution"))
                .setHistoryIndex(HISTORY_INDEX); // Set the history index name
        String historyIndex = elasticsearchEvolutionConfig.getHistoryIndex();
        historyRepository = new HistoryRepositoryImpl(restHighLevelClient.getLowLevelClient(), historyIndex, new MigrationScriptProtocolMapper(), 1000, objectMapper);
        elasticsearchEvolution = elasticsearchEvolutionConfig
                .load(restHighLevelClient.getLowLevelClient());
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Delete the user index and history after the test
        deleteIndex(INDEX_NAME);
        deleteIndex(HISTORY_INDEX);

        // Close the RestHighLevelClient instance
        if (restHighLevelClient != null) {
            restHighLevelClient.close();
        }
    }

    @Test
    void migrate_OK() throws IOException {
        assertSoftly(softly -> {
            softly.assertThat(elasticsearchEvolution.migrate())
                    .as("# of successful executed scripts ")
                    .isEqualTo(2);


            // Verify the creation of the index
            boolean indexExists = false;
            try {
                indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(INDEX_NAME), RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            softly.assertThat(indexExists)
                    .as("Index is created")
                    .isTrue();

            // Verify the mapping of the index
            GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices(INDEX_NAME);
            GetMappingsResponse getMappingsResponse = null;
            try {
                getMappingsResponse = restHighLevelClient.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map<String, MappingMetaData> mappings = getMappingsResponse.mappings();
            MappingMetaData mappingMetaData = mappings.get(INDEX_NAME);
            Map<String, Object> propertiesWrapper = mappingMetaData.sourceAsMap();
            Map<String, Object> properties = (Map<String, Object>) propertiesWrapper.get("properties");
            softly.assertThat(properties)
                    .as("Index has the correct fields")
                    .hasSize(4)
                .containsKeys("id", "name", "email", "age");
        });
    }

    private void deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
    }
}


