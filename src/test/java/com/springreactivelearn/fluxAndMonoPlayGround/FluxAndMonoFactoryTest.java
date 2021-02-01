package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {

    /**
     * Flux
     * fromIterable - creating a flux from a list
     * fromArrays - creating a flux from an array
     * fromStream - creating a flux from a stream
     */

    /*
        Flux using iterable, arrays, streams, range
        Mono using empty, justOrEmpty, supplier
     */

    List<String> names = Arrays.asList("Amma", "Appa", "Thatha", "Paati");

    @Test
    public void fluxUsingIterable(){
        Flux<String> namesFlux = Flux.fromIterable(names);

        StepVerifier.create(namesFlux.log())
                .expectNext("Amma", "Appa", "Thatha", "Paati")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArrays(){
        String[] namesArray = {"Amma", "Appa", "Thatha", "Paati"};
        Flux<String> namesFlux = Flux.fromArray(namesArray);

        StepVerifier.create(namesFlux.log())
                .expectNext("Amma", "Appa", "Thatha", "Paati")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStreams(){

        Flux<String> namesFlux = Flux.fromStream(names.parallelStream());

        StepVerifier.create(namesFlux.log())
                .expectNext("Amma", "Appa", "Thatha", "Paati")
                .verifyComplete();
    }
}
