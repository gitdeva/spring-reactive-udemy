package com.springreactivelearn.controller;

import javafx.beans.binding.IntegerBinding;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {

    //webfluxtest will not scan @Component, @Service, @Repository
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproach1(){
        Flux<Integer> flux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() //this one invokes the end point
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody()
                ;

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }
}
