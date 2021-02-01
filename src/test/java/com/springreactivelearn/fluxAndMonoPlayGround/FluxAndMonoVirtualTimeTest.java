package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoVirtualTimeTest {

    @Test
    public void testingWithoutVirtualTime(){
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(4);

        StepVerifier.create(longFlux.log())
                .expectSubscription()
                .expectNext(0l, 1l, 2l, 3l)
                .verifyComplete();
    }

    @Test
    public void testingWithVirtualTime(){

        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(4);

        StepVerifier.withVirtualTime(() -> longFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                .expectNext(0l,1l,2l,3l)
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat()
    {
        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.concat(flux1, flux2);
        /*StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();*/

        StepVerifier.withVirtualTime(() -> mergeFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                //.expectNextCount(6)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }
}
