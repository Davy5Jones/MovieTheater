package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.CustomerLog;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.repos.logs.CustomerLogsRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelAssembler extends RepresentationModelAssemblerSupport<Customer, CustomerModelDto> {

    private final CustomerLogsRepository customerLogsRepository;

    public CustomerModelAssembler(CustomerLogsRepository customerLogsRepository) {
        super(Customer.class, CustomerModelDto.class);
        this.customerLogsRepository = customerLogsRepository;
    }


    @Override
    public CustomerModelDto toModel(Customer customer) {
        CustomerLog customerLog= customerLogsRepository.findCustomerLogByCustomerId(customer.getId()).get();
        return new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName(),customerLog.getRegistered(),customerLog.getLastSeen());
    }
}
