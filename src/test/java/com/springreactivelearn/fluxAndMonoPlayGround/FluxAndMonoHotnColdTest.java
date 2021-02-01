package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class FluxAndMonoHotnColdTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> flux = Flux.just("A","B","C","D","E","F")
                .log();

        flux.subscribe(s -> System.out.println("first subscriber "+s));
        Thread.sleep(2000);

        flux.subscribe(s ->System.out.println("second subscriber "+s));
        Thread.sleep(3000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> flux = Flux.just("A","B","C","D","E","F")
                .log();

        ConnectableFlux<String> cFlux = flux.publish();
        cFlux.connect();
        cFlux.subscribe(s ->System.out.println("first subscriber "+s));
        //Thread.sleep(2000);

        cFlux.subscribe(s ->System.out.println("second subscriber "+s));
        Thread.sleep(3000);
    }
}
