package com.star.demo.resource;


import com.star.demo.model.Quote;
import com.star.demo.model.Quotes;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.star.demo.service.BusinessService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResource {

    @Resource(name = "businessService")
    private BusinessService businessService;

    @GetMapping("{username}")
    public List<String> getQuotes(@PathVariable("username") final String username){
        if(StringUtils.isEmpty(username)){
            return Collections.emptyList();
        }

        return getQuotesByUsername(username);
    }

    private List<String> getQuotesByUsername(@PathVariable("username") String username) {
        return businessService.findByName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes){
        if(quotes == null){
            throw new RuntimeException("quote cannot be null");
        }

       quotes.getQuotes()
                .stream()
                .map(quote -> new Quote().setQuote(quote).setName(quotes.getName()))
                .forEach(quote -> {

                    businessService.save(quote);
                });

        return getQuotesByUsername(quotes.getName());
    }
}
