package com.star.demo.repository;

import com.star.demo.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbServiceRepository extends JpaRepository<Quote,Integer> {


    List<Quote> findByName(String username);
}
