package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
                .log();
        infiniteFlux
                .subscribe(e -> System.out.println("Value is :"+e));
        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                //.expectSubscription()  //working same when uncommented also
                .expectNext(0L, 1L, 2L)
                .verifyComplete();

    }
}
