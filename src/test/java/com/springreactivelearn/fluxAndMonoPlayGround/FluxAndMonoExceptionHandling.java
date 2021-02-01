package com.springreactivelearn.fluxAndMonoPlayGround;

import com.springreactivelearn.exception.CustomException;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Flux exception handling
 * expectError  - stepverifier checks for exception using this method
 * onErrorResume - flux resumes the flow here in case of any exception
 * onErrorReturn - flux returns the value when there is an exception
 * onErrorMap
 * retry - retry n number of times after exception occurs - flux retries from the first after exception occurs
 * retryWithBackOff - is removed in recent versions of springBoot
 */
public class FluxAndMonoExceptionHandling {

    @Test
    public void fluxErrorHandling(){
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Flux Exception")))
                .concatWith(Flux.just("D"));

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErroResume(){
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Flux Exception")))
                .onErrorResume( e -> {
                    System.out.println("Exception in flux");
                    return Flux.just("Default");
                })
                .concatWith(Flux.just("D"));

        StepVerifier.create(flux.log())
                .expectNext("A", "B", "C")
                .expectNext("Default","D")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn(){
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Flux Exception")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Default")
                ;

        StepVerifier.create(flux.log())
                //.expectNext("A", "B", "C")
                .expectNext("A", "B", "C","Default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap(){
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Flux Exception")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new IllegalStateException())
                ;

        StepVerifier.create(flux.log())
                //.expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_WithRetry(){
        Flux<String> flux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Flux Exception")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new IllegalStateException()) // can be any custom exception here
                .retry(2)
                ;

        StepVerifier.create(flux.log())
                //.expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap_withRetryBackoff() {

        Retry retrySpec = Retry.fixedDelay(2, Duration.ofMillis(1000))
                .filter((ex) -> ex instanceof CustomException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retryWhen(retrySpec);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();

    }
}
