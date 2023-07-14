package com.example.elastic.poc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users", createIndex = false)
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private int age;
}
