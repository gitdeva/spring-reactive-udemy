package com.springreactivelearn.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest(){
        Flux<Integer> finiteFlux = Flux.range(1, 3)
                .log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify()
        ;
    }

    @Test
    public void backPressureUsingSubscriptionTest(){
        Flux<Integer> finiteFlux = Flux.range(1, 3)
                .log();
        finiteFlux.subscribe(e -> System.out.println("Element is :"+e),
                e -> System.err.println("Exception is "+e),
                () -> System.out.println("DONE!"),
                subscription -> subscription.request(4)
        );
    }


    @Test
    public void backPressureUsingSubscriptionTest2(){
        Flux<Integer> finiteFlux = Flux.range(1, 3)
                .log();
        finiteFlux.subscribe(e -> System.out.println("Element is :" + e),
                e -> System.err.println("Exception is " + e),
                () -> System.out.println("DONE!"),
                subscription -> subscription.request(2)
        );
    }


    @Test
    public void customizedBackPressure_Data(){
        Flux<Integer> finiteFlux = Flux.range(1, 3)
                .log();
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
                                 @Override
                                 protected void hookOnNext(Integer value) {
                                     request(1);
                                     if(value == 2)
                                     {
                                         System.out.println("Expected value reached "+value);
                                         cancel();
                                     }
                                 }
                             }
        );
    }

    @Test
    public void backPressureUsingLimitRate(){
        Flux<Integer> finiteFlux = Flux.range(1, 3)
                .log();
        //Limitrate does not stop the flux with those requests. it is just the number of request that are sent at a time.
        finiteFlux.limitRate(1).subscribe(e ->System.out.println("Element is :" + e),
                throwable -> System.err.println("exception is "+throwable),
                () -> System.out.println("DONE!")
        );
        /*finiteFlux.subscribe(e -> System.out.println("Element is :" + e),
                e -> System.err.println("Exception is " + e),
                () -> System.out.println("DONE!"),
                subscription -> subscription.request(2)
        );*/
    }
}
