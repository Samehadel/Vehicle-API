package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegerationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${cars.endpoint}")
    String carsBaseUrl;

    private WebClient carsClient;

    @Before
    public void init(){
        carsClient = WebClient.create(carsBaseUrl);
        Car car = getCar();

        carsClient.post().uri("/cars").body(Mono.just(getCar()), Car.class).exchange();
    }

    @Test
    public void getCarById(){
        Car responseCar = carsClient.get().uri("/cars/1")
                            .retrieve().bodyToMono(Car.class).block();

        Assertions.assertEquals((Long) 1l, responseCar.getId());
    }


    @Test
    public void addCar() throws Exception{
        carsClient = WebClient.create(carsBaseUrl);
        Car car = getCar();

        Car responseCar = carsClient.post().uri("/cars")
                                    .body(Mono.just(getCar()), Car.class)
                                    .retrieve().bodyToMono(Car.class).block();

        Assertions.assertNotNull(responseCar.getId());

    }

    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}
