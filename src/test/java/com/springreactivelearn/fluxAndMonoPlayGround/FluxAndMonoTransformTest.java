package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {

    /**
     * Flux
     * map -- similar to map in streams in java 8
     * repeat(n) - repeats all the elements in the flux 'n' number of times
     * flatMap
     * flatMap using Schedulers.parallel
     */

    List<String> names = Arrays.asList("Govinda", "Venkatesa", "Srinivasa", "Venkatachalam");
    //Flux map, flatmap
    @Test
    public void fluxUsingMap(){
        Flux<String> flux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(flux)
                .expectNext("GOVINDA", "VENKATESA", "SRINIVASA", "VENKATACHALAM")
                .verifyComplete();
    }

    @Test
    public void fluxUsingMapLength(){
        Flux<Integer> flux = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(flux)
                .expectNext(7, 9, 9, 13)
                .verifyComplete();
    }

    @Test
    public void fluxUsingMapLength_Repeat(){
        Flux<Integer> flux = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(flux)
                .expectNext(7, 9, 9, 13,7, 9, 9, 13)
                .verifyComplete();
    }


    @Test
    public void fluxUsingFlatMap(){
        Flux<String> flux = Flux.fromIterable(names)
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();

        StepVerifier.create(flux.log())
                .expectNextCount(8)
                .verifyComplete();
    }

    private List<String> convertToList(String s){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void fluxUsingFlatMapParallel(){
        Flux<String> flux = Flux.fromIterable(names)
                .window(2)
                .flatMap(s ->
                    s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(flux.log())
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void fluxUsingFlatMapParallelMaintainOrder(){
        Flux<String> flux = Flux.fromIterable(names)
                .window(2)
                /*.concatMap(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> Flux.fromIterable(s))*/
                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(flux.log())
                .expectNextCount(8)
                .verifyComplete();
    }
}
