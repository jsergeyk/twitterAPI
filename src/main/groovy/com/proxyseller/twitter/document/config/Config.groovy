package com.proxyseller.twitter.document.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
class Config extends AbstractMongoClientConfiguration {

    @Value('${spring.data.mongodb.database}')
    private String databaseName;
    @Value('${host}')
    private String host;
    @Value('${port}')
    private String port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    MongoClient mongoClient() {
        def connectionString = new ConnectionString("mongodb://"+ host + ":" + port + "/");
        def mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }

    @Bean
    Logger logger(){
        return LoggerFactory.getLogger("application");
    }
}