package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.repos.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void register(CustomerModelDao customer) throws CustomCinemaException {
        if (customerRepository.existsByEmail(customer.getEmailAddress())) {
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        }
        customerRepository.save(Customer.builder()
                .name(customer.getCustomerName())
                .email(customer.getEmailAddress())
                .password(bCryptPasswordEncoder.encode(customer.getPassword()))
                .build());
    }
}
