package com.in28minutes.microservices.currencyexchangeservice;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger= LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    //@Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")//powtórz kilka razy jak nie to fallbackMethod
    //@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
    /*
    jeżeli zaczynają się nie udane odpowiedzi, przechodzi w closed do open, i czeka wysyłając fall back
    przechodzi w half open i kilka procent zapytan testuje jezeli się udają przechodzi do closed i działa normalnie a jak nie to
    z powrotem do open i wysyła dalej fallbackMethod
     */
    //@RateLimiter(name = "default")//limits amount of calls in period of time
    @Bulkhead(name = "default")//maksymalna ilosc równoległych callów
    public String sampleApi(){
        logger.info("Sample API call recived!");
//        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
//
//        return forEntity.getBody();
        return "Sample-api";
    }

    public String hardcodedResponse(Exception e){
        return "fallback-response";
    }
}
