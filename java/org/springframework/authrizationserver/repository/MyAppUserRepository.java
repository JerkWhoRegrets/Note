package org.springframework.authrizationserver.repository;

import org.springframework.authrizationserver.model.MyAppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyAppUserRepository extends CrudRepository<MyAppUser, Long> {
    Optional<MyAppUser> findByUsername(String username);
}
