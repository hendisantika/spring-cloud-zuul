package com.hendisantika.zuul.eureka.customer.shared.server.server.rest;

/**
 * Created by IntelliJ IDEA.
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 8/30/17
 * Time: 6:56 AM
 * To change this template use File | Settings | File Templates.
 */

import com.hendisantika.zuul.eureka.customer.shared.server.server.domain.Customer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    private List<Customer> customers;

    public CustomerController() {
        customers = new LinkedList<>();
        customers.add(new Customer(1, "Naruto", "Test"));
        customers.add(new Customer(2, "Sasuke", "Test2"));
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET, produces = "application/json")     public Customer getCustomer(@PathVariable int id) {

        Optional<Customer> customer = customers.stream().filter(customer1 -> customer1.getId() == id).findFirst();

        return customer.get();

    }

}