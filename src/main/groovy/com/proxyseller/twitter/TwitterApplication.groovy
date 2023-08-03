package com.proxyseller.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
class TwitterApplication {

	static void main(String[] args) {
		SpringApplication.run(TwitterApplication, args)
	}
}