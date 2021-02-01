package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    /**
     * Flux
     * Flux.just
     * StepVerifier.create().expectNext()
     * StepVerifier.create().expectNext().expectError()
     * StepVerifier.create().expectNextCount()
     * StepVerifier.create().expectNext().expectErrorMessage()
     */

    @Test
    public void fluxTest(){
        Flux<String> flux = Flux.just("Spring ", "Spring Boot ", "Reactive Spring ")
                               // .concatWith(Flux.error(new RuntimeException("RunTImeException caused manually")))
                                .concatWith(Flux.just("after error"))
                                .log();

        flux.subscribe(System.out::println, e -> System.err.println("ERROR in TEST "+e));
    }

    @Test
    public void fluxTest_WithoutError(){
        Flux<String> flux = Flux.just("Spring ", "Spring Boot ", "Reactive Spring ")
                .log();

        StepVerifier.create(flux)
                .expectNext("Spring ")
                .expectNext("Spring Boot ")
                .expectNext("Reactive Spring ")
                .verifyComplete()
                ;
    }

    @Test
    public void fluxTest_WithError(){
        Flux<String> flux = Flux.just("Spring ", "Spring Boot ", "Reactive Spring ")
                .concatWith(Flux.error(new Exception("Flux error")))
                .log();

        StepVerifier.create(flux)
                .expectNext("Spring ", "Spring Boot ", "Reactive Spring ")
//                .expectNext("Spring Boot ")
//                .expectNext("Reactive Spring ")
                .expectError()
                //.expectErrorMessage("Flux error")
                .verify()
                //.verifyComplete()
        ;
    }


    @Test
    public void fluxTestCount_WithError(){
        Flux<String> flux = Flux.just("Spring ", "Spring Boot ", "Reactive Spring ")
                .concatWith(Flux.error(new Exception("Flux error")))
                .log();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .expectError()
                //.expectErrorMessage("Flux error")
                .verify()
        //.verifyComplete()
        ;
    }

    ///////////////////MONO TEST

    @Test
    public void monoTest_WithoutError(){
        Mono<String> mono = Mono.just("Spring")
                .log();
        StepVerifier.create(mono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_WithError(){
        Mono<Object> mono = Mono.error(new RuntimeException())
                .log();
        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
