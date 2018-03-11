package service;

import com.sun.org.apache.xpath.internal.operations.Quo;
import model.Quote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BusinessService {
    List<Quote> findByUsername(String username);

    Quote save(Quote quote);
}
