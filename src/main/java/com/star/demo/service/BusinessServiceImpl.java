package com.star.demo.service;


import com.star.demo.model.Quote;
import org.springframework.stereotype.Component;
import com.star.demo.repository.DbServiceRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service(value = "businessService")
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private DbServiceRepository dbServiceRepository;

    @Override
    public List<Quote> findByUsername(String username) {
//        if (StringUtils.isEmpty(username)){
//            throw new RuntimeException("username cannot be null or empty");
//        }

        return dbServiceRepository.findByName(username);
    }

    @Override
    public Quote save(Quote quote) {
        return dbServiceRepository.save(quote);
    }


}
