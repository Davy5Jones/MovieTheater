package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelAssembler extends RepresentationModelAssemblerSupport<Customer, CustomerModelDto> {
    public CustomerModelAssembler() {
        super(Customer.class, CustomerModelDto.class);
    }

    @Override
    public CustomerModelDto toModel(Customer customer) {
        return new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName());
    }
}
