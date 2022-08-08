package com.example.demoflux.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@WebFluxTest

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {Application.class, TestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FluxAndMonoControllerTest extends BaseControllerTest {

    @Test
    public void when_int_flux_return_result() {
        Flux<Integer> integerFlux = webTestClient
                .get()
                .uri("/int-flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier
                .create(integerFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();


        integerFlux.subscribe(x -> {
            System.out.println(Thread.currentThread().getName());
        });
    }

    @Test
    public void when_int_flux_expect_body_list() {

        webTestClient
                .get()
                .uri("/int-flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(6);


    }

    @Test
    public void when_int_flux_expect_body_list_return_result() {

        List<Integer> expectedValues = Arrays.asList(2, 3, 4, 5, 6, 7);

        List<Integer> values = webTestClient
                .get()
                .uri("/int-flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .returnResult()
                .getResponseBody();

        assertEquals(expectedValues, values);


    }

    @Test
    public void when_int_flux_expect_body_list_consume_with() {

        List<Integer> expectedValues = Arrays.asList(2, 3, 4, 5, 6, 7);

        webTestClient
                .get()
                .uri("/int-flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .consumeWith(x -> {
                    assertEquals(x.getResponseBody(), expectedValues);
                });
//                .returnResult()
//                .getResponseBody();
//
//        assertEquals(expectedValues, values);

    }

    @Test
    public void when_flux_then_cancel() {

        Flux<Integer> integerFlux =
                webTestClient
                        .get()
                        .uri("/int-flux")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .returnResult(Integer.class)
                        .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(2)
                .expectNext(3)
                .thenCancel()
                .verify();


    }

}
