package com.demoflux.controllers;


import com.demoflux.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@Slf4j
public class FluxAndMonoController {


    @GetMapping(path = "/int-flux", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<Integer> handleGetIntFlux() throws InterruptedException {
        System.out.println("Thread : " + Thread.currentThread().getName());

        Flux<Integer> integerFlux =
                Flux
                        .just(1, 2, 3, 4, 5, 6)
                        .map(x -> {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
////                            System.out.println("The publisher is publishing the values in : " + Thread.currentThread().getName());
                            return x + 1;
                        })
                        .log();
//        Mono<List<Integer>> listMono = integerFlux.collectList();
//        List<Integer> integerList = listMono.block();

        System.out.println("Thread : " + Thread.currentThread().getName() + " is leaving");
        return integerFlux;
    }

    @GetMapping(path = "/monoJust", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Integer> getMonoJust() throws InterruptedException {
//        Mono.just(asyncMethod())
//                .doOnNext(System.out::println)
//                .subscribeOn(Schedulers.newSingle("ASYNC")).subscribe();
        Mono.fromRunnable(() -> {

            asyncMethod();

        }).log().subscribeOn(Schedulers.newSingle("ASYNC1")).subscribe();
        return Mono.just(AppUtil.getRandomNumber(1, 100));
    }

    @GetMapping(path = "/monoJustOrEmpty", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Integer getMonoJustOrEmptyOpt() throws InterruptedException {
        System.out.println("Thread : " + Thread.currentThread().getName());

        Mono<Integer> optionalMono = Mono.justOrEmpty(asyncOptionalMethod())
                .subscribeOn(Schedulers.newSingle("ASYNC2"))
                .doOnNext(System.out::println)
                .defaultIfEmpty(1000)
                .log();

        System.out.println("Thread : " + Thread.currentThread().getName() + " is leaving");
        return 9;// Mono.just(1);
    }

    @GetMapping(path = "/monoJustOrEmptyT", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<Integer> getMonoJustOrEmptyT() throws InterruptedException {
        System.out.println("Thread : " + Thread.currentThread().getName());

//        Mono.justOrEmpty(asyncMethod())
//                .defaultIfEmpty(1000).log()
//                .subscribeOn(Schedulers.newSingle("ASYNC")).subscribe();

        Mono.just(10)
                .map(s -> generateData(s))
//                .doOnNext(s -> log.info("s: {}",s))
//                .doOnNext(s -> generateData(s))
                .subscribeOn(Schedulers.newSingle("ASYNC"))
                .subscribe(s -> log.info("s: {}",s));

//        Flux.fromIterable(generateData(10)).log()
////        Flux.range(1, 100_000)
//                .doOnNext(a -> System.out.println(a + ", thread: " + Thread.currentThread().getName()))
//                .flatMap(a -> Mono.just(a).subscribeOn(Schedulers.newSingle("OKE")))
//                .subscribeOn(Schedulers.newSingle("OKE123"))
//                .subscribe();

        return Mono.just(AppUtil.getRandomNumber(1, 10));
    }

    Mono<String> getPhonetic(String w) {
//        log.debug("getPhonetic: {}", w);
        return Mono.just(w + "-1");
    }

    private List<String> generateData(int n) {
        System.out.println("generateData Thread : " + Thread.currentThread().getName());
        List<String> res = new ArrayList<>();
        while (n > 0) {
            System.out.println("n: " + n);
            res.add("Data ke-" + n--);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private Integer asyncMethod() {
        // some calculation
        int i = 1;
        while (i++ <= 10) {
            System.out.println(i);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        return 0;
    }

    private Optional<Integer> asyncOptionalMethod() {
        // some calculation
        return Optional.of(8978);
    }

    @GetMapping(path = "/int-mono")
    public Mono<Integer> handleGetMono() {

        return Mono.just(5);
    }


}
