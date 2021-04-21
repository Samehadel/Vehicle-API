package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
    Testing the pricing microservice for retrieving all prices for all vehicles and a specific price for a
    specific vehicle.
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getPricesTest(){
        String url = "http://localhost:8082/prices";

        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(url, Object.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
    @Test
    public void getOnePriceTest(){
        long vehicleId = 1;
        String url = "http://localhost:8082/prices/" + vehicleId;

        ResponseEntity<Price> responseEntity = restTemplate.getForEntity(url, Price.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}
