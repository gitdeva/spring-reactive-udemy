package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/*
Flux merge using following methods
1. merge
2. merge flux with delayOnElements
3. concat - will add the elements from flux1 first and then flux2
4. concat flux with delayOnelements
5. zip - will perform operations on flux elements of flux1 and flux2
 */
public class FluxAndMonoMergeTest {

    @Test
    public void combineUsingMerge()
    {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergeFlux = Flux.merge(flux1, flux2);
        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_WithDelay()
    {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.merge(flux1, flux2);
        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat()
    {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergeFlux = Flux.concat(flux1, flux2);
        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcatWithDelay()
    {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.concat(flux1, flux2);
        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip()
    {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.zip(flux1, flux2, (f1, f2) -> f1.concat(f2));
        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                //.expectNext("A","B","C","D","E","F")
                .expectNextCount(3)
                .verifyComplete();
    }
}
