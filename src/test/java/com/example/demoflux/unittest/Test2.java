package com.example.demoflux.unittest;

import com.example.demoflux.entity.Player;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Test2 {

    @Test
    public void a() {
        Flux<Player> players = Flux.just(
                        "Zahid Khan",
                        "Arif Khan",
                        "Obaid Sheikh")
                .map(fullname -> {
                    String[] split = fullname.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(players)
                .expectNext(new Player("Zahid", "Khan"))
                .expectNext(new Player("Arif", "Khan"))
                .expectNext(new Player("Obaid", "Sheikh"))
                .verifyComplete();

    }

    @Test
    public void b() {
        Flux<String> flux = Flux.just("Khanh", "Quan");
        flux.flatMap(s -> Mono.just(new Integer(s.length())))

                .map(value -> {
                    System.out.println(value);
                    return value;
                })
                .subscribe();
    }
}
