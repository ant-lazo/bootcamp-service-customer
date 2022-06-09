package com.nttdata.bootcamp.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.bootcamp.models.CustomerPerson;

import reactor.core.publisher.Mono;

public interface ICustomerPersonRepo extends ReactiveMongoRepository<CustomerPerson, String>{

	Mono<CustomerPerson> findByIdDocument(String idDocument);

}
