package com.epam.learn.elastic.task4.service;

import com.epam.learn.elastic.task4.dto.Employee;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

    private static final String EMPLOYEES_INDEX = "employees";
    private final ElasticsearchClient elasticsearchClient;

    public EmployeeService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Employee> getAllEmployee() {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(EMPLOYEES_INDEX)
                .query(QueryBuilders.matchAll().build())
        );

        try {
            SearchResponse<Employee> search = elasticsearchClient.search(searchRequest, Employee.class);
            return search.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee getEmployeeById(String id) {
        GetResponse<Employee> response = null;
        try {
            response = elasticsearchClient.get(g -> g
                            .index(EMPLOYEES_INDEX)
                            .id(id),
                    Employee.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.found()) {
            return response.source();
        } else {
            log.info("Employee not found");
            return new Employee();
        }
    }

    public String createEmployee(String employeeId, Employee employee) {
        IndexResponse response = null;
        try {
            response = elasticsearchClient.index(i -> i
                    .index(EMPLOYEES_INDEX)
                    .id(employeeId)
                    .document(employee));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.result().jsonValue();
    }

    public void deleteEmployee(String employeeId) {
        try {
            elasticsearchClient.delete(d -> d.index(EMPLOYEES_INDEX).id(employeeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> searchEmployeeByFieldAndValue(String field, String value) {
        try {
            SearchResponse<Employee> response = elasticsearchClient.search(s -> s
                            .index(EMPLOYEES_INDEX)
                            .query(q -> q
                                    .match(t -> t
                                            .field(field)
                                            .query(value)
                                    )
                            ),
                    Employee.class
            );
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAggregation(String field, String metricType, String metricField) {
        Aggregation aggregation = null;

        switch (metricType.toLowerCase()) {
            case "avg":
                aggregation = AggregationBuilders.avg().field(metricField).build()._toAggregation();
                break;
            case "sum":
                aggregation = AggregationBuilders.sum().field(metricField).build()._toAggregation();
                break;
            case "min":
                aggregation = AggregationBuilders.min().field(metricField).build()._toAggregation();
                break;
            case "max":
                aggregation = AggregationBuilders.max().field(metricField).build()._toAggregation();
                break;
            default:
                throw new IllegalArgumentException("Unsupported metric type: " + metricType);
        }

        Aggregation finalAggregation = aggregation;
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(EMPLOYEES_INDEX)
                .size(0)
                .aggregations(field, finalAggregation)
        );

        SearchResponse<Void> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, Void.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchResponse.aggregations().toString();
    }
}
