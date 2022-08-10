package com.demoflux.controllers;

import com.demoflux.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class MonoController {

    //ubah 1
    @GetMapping("mono/sample")
    public Mono<Integer> sample() {
        Mono.just(5)
                .doOnNext(maxTime -> process(maxTime))
                .subscribeOn(Schedulers.newSingle("ASYNC"))
                .subscribe(
                        response -> {
                            log.info("success to archive user activity");
                        },
                        throwable -> {
                            log.error("failed to archive user activity", throwable);
                        }
                );
        return Mono.just(AppUtil.getRandomNumber(1, 10));
    }

    Boolean process(long maxTime) {
        int i = 1;
        while (i++ <= 10) {
            System.out.println("Data ke" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
                e.printStackTrace();
            }
        }

        return true;
    }

    @GetMapping("mono/sample1")
    public Mono<Integer> sample1() {
        Mono.fromCallable(this::getData).log()
                .map(strings -> doSomething(strings))
                .doOnNext(strings -> System.out.println("strings = " + strings))
                .subscribeOn(Schedulers.newSingle("ASYNC"))
                .subscribe(
                        response -> {
                            System.out.println("success: " + response);

                        },
                        throwable -> {
                            System.out.println("failed: " + throwable);
                        }
                );

        return Mono.just(AppUtil.getRandomNumber(1, 10));
    }

    public boolean doSomething(List<String> s) {
        System.out.println("sss: " + s);
        return true;
    }

    public List<String> getData() {
        List<String> res = new ArrayList<>();
        int i = 1;
        while (i++ <= 10) {
            res.add("Data ke" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
                e.printStackTrace();
            }
        }
        return res;
    }
}
