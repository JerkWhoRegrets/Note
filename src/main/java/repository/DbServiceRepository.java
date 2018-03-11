package repository;

import model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbServiceRepository extends JpaRepository<Quote,Integer> {


    List<Quote> findByUsername(String username);
}
