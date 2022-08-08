package com.demoflux.service;


import com.demoflux.model.Customer;
import com.demoflux.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerService {


    @Autowired
    CustomerRepository customerRepository;

    public Flux<Customer> getCustomers() {
        Flux<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Mono<Customer> getCustomerById(String id) {

        Mono<Customer> customerMono = customerRepository.findById(id);

        return customerMono;

    }


}
