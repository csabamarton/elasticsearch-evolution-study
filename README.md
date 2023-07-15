# Elasticsearch Evolution Example

This project demonstrates the usage of Elasticsearch Evolution for managing Elasticsearch index schema changes using
migration scripts.

## Getting Started

### Prerequisites

- Docker
- Java Development Kit (JDK)
- Maven

### Project Sturcture
   ```markdown
     - docker-compose
         - docker-compose.yml (Docker Compose configuration file for Elasticsearch and Kibana)
     - postman-collection
         - ElasticSearch.postman_collection.json (Postman collection for testing the API endpoints)- src
     - src
      - main
         - java/com/csmarton/elastic/poc
            - ElasticApplication.java (Main application class)
            - model/User.java (User model class)
     - resources
         - application.properties (Application configuration)
         - es/evolution (Folder containing the migration scripts)
            - V1__Create_User_Index_and_Mapping.http (Migration script for creating the users index and mapping)
            - V2__Add_New_Field.http (Migration script for adding the age field to the users mapping)
   ```

### Running the Application

# Getting Started

1. Clone the repository: git clone https://github.com/csabamarton/elasticsearch-evolution-study.git
2. Navigate to the project directory: cd elastic-poc

# Docker Setup for Elasticsearch and Kibana

You can use Docker to set up Elasticsearch and Kibana. Docker Compose configuration files are provided in the
docker-compose folder.

1. Navigate to the docker-compose folder: cd docker-compose
2. Start Elasticsearch and Kibana containers: docker-compose up -d
3. Elasticsearch will be running at http://localhost:9200
4. and Kibana at http://localhost:5601

# Testing

1. Build and run the application:
   ```shell
   mvn clean install
   mvn spring-boot:run
   ```
   The backend application will be running at http://localhost:8080.


2. Verify that the migration scripts are executed successfully by checking the logs in the console. You should see log
   messages indicating the execution of the migration scripts.

3. Verify the index schema changes in Kibana by executing the following command:
   ```shell
   GET /users
   ```
   The result should be similar: 
   ```json
   {
     "users": {
       "aliases": {},
       "mappings": {
         "properties": {
           "age": {
             "type": "integer"
           },
           "email": {
             "type": "keyword"
           },
           "id": {
             "type": "keyword"
           },
           "name": {
             "type": "text"
           }
         }
       },
       "settings": {
         "index": {
           "creation_date": "1689347974643",
           "number_of_shards": "1",
           "number_of_replicas": "1",
           "uuid": "SJexFtCaRE6sVrIlpcnB3A",
           "version": {
             "created": "7040099"
           },
           "provided_name": "users"
         }
       }
     }
   }
   ```
   
