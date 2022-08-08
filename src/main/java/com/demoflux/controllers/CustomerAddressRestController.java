package com.demoflux.controllers;


import com.demoflux.model.CustomerAddress;
import com.demoflux.repository.CustomerAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CustomerAddressRestController {


    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @GetMapping(path = "/customers/{customerId}/addresses", params = {"type"})
    public Flux<CustomerAddress> handleGetAddressesByIdAndType(@PathVariable String customerId,
                                                               @RequestParam String type) {


        Flux<CustomerAddress> addressFlux = customerAddressRepository.findByCustomerIdAndType(customerId, type);

        return addressFlux;

    }

}
