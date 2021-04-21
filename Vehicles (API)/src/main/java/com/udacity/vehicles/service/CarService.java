package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.Address;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    @Autowired
    private PriceClient priceClient;

    @Autowired
    private MapsClient mapsClient;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;


    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return carRepository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        Car car = optionalCar.isPresent() ? optionalCar.get() : null;

        if(!optionalCar.isPresent())
            throw new CarNotFoundException("Car Not Found For ID " + id);

        /*
            * Continue to use the Pricing Microservice in client.prices.PriceCleint
            * to get the price for the vehicle.
         */

        String price = priceClient.getPrice(id);
        car.setPrice(price);



        /**
         * Continue to use the Maps Web client in client.maps.MapsCleint
         * to get the address for the vehicle.
         */

        Location location = mapsClient.getAddress(car.getLocation());
        car.setLocation(location);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        int manufacturerCode = car.getDetails().getManufacturer().getCode();
        //car.getDetails().setManufacturer(getManufacturer(manufacturerCode));

        Manufacturer manufacturer = getManufacturer(manufacturerCode); //Get manufacturer from DB

        //Existed Car
        if (car.getId() != null) {

            return carRepository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.getDetails().setManufacturer(manufacturer);
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setLocation(car.getLocation());

                        return carRepository.save(car);
                    }).orElseThrow(CarNotFoundException::new);
        }
        //New Car
        return carRepository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {

        Optional<Car> optionalCar = carRepository.findById(id);

        if(optionalCar.isPresent())
            carRepository.delete(optionalCar.get());
        else
            throw new CarNotFoundException("Car Not Found For ID " + id);

    }

    /**
     * Retrieve the Manufacturer by its code to be injected in car's details
     * @param code
     * @return Manufacturer object corresponds to code
     */
    private Manufacturer getManufacturer(Integer code){
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(code); //Get manufacturer from DB

        if (manufacturer.isPresent())
            return manufacturer.get();
        else
            throw new ManufacturerNotFoundException("Manufacturer Not Exist For Code " + code);
    }
}
