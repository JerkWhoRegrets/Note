package service;


import model.Quote;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import repository.DbServiceRepository;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private DbServiceRepository dbServiceRepository;

    @Override
    public List<Quote> findByUsername(String username) {
//        if (StringUtils.isEmpty(username)){
//            throw new RuntimeException("username cannot be null or empty");
//        }

        return dbServiceRepository.findByUsername(username);
    }

    @Override
    public Quote save(Quote quote) {
        return dbServiceRepository.save(quote);
    }


}
