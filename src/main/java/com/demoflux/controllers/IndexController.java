package com.demoflux.controllers;

import com.demoflux.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class IndexController {

    @GetMapping("a")
    public Mono<Long> a() {
        log.info("a");
        Mono.fromRunnable(() -> {

            for (int i = 1; i < 11; i++) {
                System.out.println("i ke-" + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).log().subscribeOn(Schedulers.newSingle("ASYNC1")).subscribe();


        return Mono.just(System.currentTimeMillis());
    }

    @GetMapping
    public Map index() {
        createFile();
        Map m = new HashMap<>();
        m.put("message", "hello");
        return m;
    }

    private void createFile() {

        Mono.fromRunnable(() -> {
                    String file = System.getProperty("java.io.tmpdir") + "greeting.txt";

                    System.out.println("file = " + file);

                    try (FileWriter fstream = new FileWriter(file);
                         BufferedWriter info = new BufferedWriter(fstream)) {
                        for (int i = 1; i < 11; i++) {
                            System.out.println("i ke-" + i);
                            Thread.sleep(1000);
                            info.write(String.format("Hello ke-" + i + "%n"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .log()
                .doOnError(throwable -> {
                    System.out.println(12345);
                    log.error("error: {}", throwable);
                })
                .subscribeOn(Schedulers.newSingle("ASYNC")).subscribe();
    }

    @GetMapping("buffer")
    public Mono<Integer> buffer() {
//        Flux.generate((SynchronousSink<String> sink) -> {
//                    try {
//                        String val = dataSource.getNextItem();
//                        if (val == null) {
//                            sink.complete();
//                            return;
//                        }
//                        sink.next(val);
//
//                    } catch (InterruptedException e) {
//                        sink.error(e);
//                    }
//                })
//                .buffer(10) //Flux<List<String>>
//                .flatMap(dataTarget::write)
//                .blockLast();

        return Mono.just(AppUtil.getRandomNumber(1,10));
    }
}
