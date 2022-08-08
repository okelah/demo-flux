package com.example.demoflux.controller;

import com.demoflux.Application;
import com.example.demoflux.TestApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


//@WebFluxTest

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, TestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseControllerTest {

    @Autowired
    WebTestClient webTestClient;
}
