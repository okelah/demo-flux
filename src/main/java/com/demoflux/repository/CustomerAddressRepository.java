package com.demoflux.repository;


import com.demoflux.model.CustomerAddress;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerAddressRepository extends ReactiveMongoRepository<CustomerAddress, String> {

    Flux<CustomerAddress> findByCustomerIdAndType(String customerId, String type);
}
