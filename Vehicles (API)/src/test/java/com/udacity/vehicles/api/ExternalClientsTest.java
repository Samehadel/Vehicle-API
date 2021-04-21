package com.udacity.vehicles.api;

import com.sun.tools.rngom.util.Uri;
import com.udacity.vehicles.client.maps.Address;
import com.udacity.vehicles.client.prices.Price;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExternalClientsTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void pricingClientTest(){
        String url = "http://localhost:8082/prices/1";

        Price price = restTemplate.getForObject(url, Price.class);

        Assertions.assertNotNull(price.getCurrency());
        Assertions.assertNotNull(price.getCurrency());
    }

    @Test
    public void mapsClientTest(){
        String url = "http://localhost:9191/maps";

        HttpHeaders headers = new HttpHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                        .queryParam("lat", 15.55)
                                        .queryParam("lon", 1.01);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<Address> response = restTemplate.exchange(builder.toUriString(),
                                                             HttpMethod.GET,
                                                             entity,
                                                             Address.class);

        Assertions.assertNotNull(response.getBody().getAddress());
        Assertions.assertNotNull(response.getBody().getCity());
        Assertions.assertNotNull(response.getBody().getState());
        Assertions.assertNotNull(response.getBody().getZip());
    }
}
