package com.star.demo.service;

import com.star.demo.model.Quote;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BusinessService {
    List<Quote> findByUsername(String username);

    Quote save(Quote quote);
}
