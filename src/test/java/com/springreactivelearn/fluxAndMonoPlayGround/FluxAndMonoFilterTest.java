package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    /**
     * Flux filter is similar to filtering a stream in java 8
     */

    List<String> names = Arrays.asList("ABC","AAC","DDC","DCCT");

    @Test
    public void filterTest(){
        Flux<String> flux = Flux.fromStream(names.stream())
                                .filter(s -> s.startsWith("A"));
        StepVerifier.create(flux.log())
                .expectNext("ABC","AAC")
                .verifyComplete();
    }

    @Test
    public void filterLengthTest(){
        Flux<String> flux = Flux.fromStream(names.stream())
                .filter(s -> s.length() > 3);
        StepVerifier.create(flux.log())
                .expectNext("DCCT")
                .verifyComplete();
    }
}
