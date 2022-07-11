package com.example.restservice;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "greetings", path = "greetings")
public interface GreetingRepository extends PagingAndSortingRepository<Greeting, Long> {

}
