package com.example.demoflux.unittest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Test1 {

    @Test
    public void a() {

        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(elements::add);
        Assertions.assertTrue(elements.contains(1));
    }

    @Test
    public void b() {


        List<String> elements = new ArrayList<>();
        int i = 0;
        Flux.just("A", "B", "C", "D")
                .log()
                .subscribe(new Subscriber<String>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(3);
                    }

                    @Override
                    public void onNext(String str) {
                        elements.add(str);
                        onNextAmount++;
                        if (onNextAmount % 3 == 0) {
                            s.request(3);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Test
    public void c() {

        List<Integer> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .doOnRequest(i -> System.out.println("request: " + i))
                .map(i -> i * 2)
                .subscribe(elements::add);
        elements.forEach(i -> System.out.println(i));
    }

    @Test
    public void d() {

        List<String> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two))
                .subscribe(elem -> elements.add(elem));

        System.out.println(elements);
    }

    @Test
    public void e() throws InterruptedException {

        List<Integer> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .doOnNext(y -> System.out.println("y: " + y))
                .subscribeOn(Schedulers.parallel())
                .subscribe(elements::add);

        System.out.println("elements: " + elements);
//        Thread.sleep(10000);
    }

    @Test
    public void f() throws InterruptedException {
        List<String> arrList = new ArrayList<>();

        Flux.just("A", "B", "C")
//                .log()
                .map(i -> i + "1")
                .subscribeOn(Schedulers.parallel())
                .subscribe(
                        t -> {
                            System.out.println(t + " thread id: " + Thread.currentThread().getId());
                            arrList.add(t);
                        }
                );
        System.out.println("size of arrList(before the wait): " + arrList.size());
        System.out.println("Thread id: " + Thread.currentThread().getId() + ": id of main thread ");
        Thread.sleep(100);
        System.out.println("size of arrList(after the wait): " + arrList.size());
    }

    @Test
    public void g() {
        List<Integer> arrList = new ArrayList<>();
        System.out.println("Before: " + arrList);
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .subscribeOn(Schedulers.parallel())
                .doOnNext(arrList::add)
                .blockLast();

        System.out.println("After: " + arrList);

    }

    @Test
    public void h() {
        List<String> words = new ArrayList<>();
        words.add("car");
        words.add("key");
        words.add("ballon");
        Flux.fromStream(words.stream()).log()
                .flatMap(w -> getPhonetic(w))
                .subscribeOn(Schedulers.newSingle("ASYNC"))
                .subscribe(y -> System.out.println("y: " + y));
    }

    Mono<String> getPhonetic(String w) {
//        log.debug("getPhonetic: {}", w);
        return Mono.just(w + "-1");
    }

    String af() {
        return "af";
    }

    @Test
    public void i() throws InterruptedException {
        final String last = Flux
                .just("A", "B", "C")
                .filter( /*"C"::equals*/ nil -> false)
                .map(s -> {
                    System.out.println("s: " + s);
                    return s + "-1";
                })
                .blockLast();
        Assertions.assertNull(last);

    }

    // Both example give the same result
    Integer getAnyInteger() throws Exception {
        throw new RuntimeException("An error as occured for no reason.");
    }

    @Test
        // Now, comparison between the two methods
    void compareMonoCreationMethods() {
        Mono<Integer> fromCallable = Mono.fromCallable(this::getAnyInteger);
        // result -> Mono.error(RuntimeException("An error as occured for no reason."))

        Mono<Integer> defer = Mono.defer(() -> {
            try {
                Integer res = this.getAnyInteger();
                return Mono.just(res);
            } catch (Exception e) {
                return Mono.error(e);
            }
        });

//        defer.subscribe(s -> { System.out.println(s.); });
        // result -> Mono.error(RuntimeException("An error as occured for no reason."))
    }


    private List<String> mockList(int n, boolean bol) {

        if (bol) return null;

        List<String> list = new ArrayList<>();
        int i = 0;
        while (i++ < n) {
            list.add("No " + i);
        }

        return list;
    }

    @Test
    public void ccc() {
        Mono.fromCallable(() -> {
                    return mockList(5, false);
                }).map(b -> process(b))
                .doOnNext(strings -> { System.out.println("strings = " + strings); })
//                .switchIfEmpty(Mono.just(new ArrayList<String>()))
                .subscribe(result -> log.info(result.toString()));
    }

    private List<String> process(List<String> list) {
        List<String> res = new ArrayList<>();
        for (String s : list) {
            res.add("a" + s);
        }
        return res;
    }

    void aaa(){
        Flux.fromStream(mockList(5, false).stream())//.log()
                //.flatMap(w -> getPhonetic(w))
                .subscribeOn(Schedulers.newSingle("ASYNC"))
                .doOnNext(s -> s.toUpperCase(Locale.ROOT))
                .subscribe();
    }

    @Test
    void vvv() throws InterruptedException {
        aaa();
        System.out.println("a");
        Thread.sleep(5000);
    }
}
